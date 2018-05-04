package hello;
import com.csvreader.CsvReader;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.data.influxdb.InfluxDBTemplate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;

import static jdk.nashorn.internal.objects.Global.print;

public class WriteCSV {
    String dbname;
    String rpName;
    String measurementName;
    public String WriteCSV(InfluxDBTemplate influxDBTemplate,String measurementName,String file_path,String file_name){
        this.dbname=influxDBTemplate.getDatabase();
        this.rpName=influxDBTemplate.getRetentionPolicy();
        InfluxDB influx=influxDBTemplate.getConnection();
        String year=file_name.substring(0,2);
        String month=file_name.substring(2,4);
        String day=file_name.substring(4,6);
        String takeoffday="20"+year+"-"+month+"-"+
                day;
        long takeoffday1=Long.parseLong("20"+year+month+day);
        String takeofftime="20"+year+"-"+month+
                "-"+day+" 00:00";
        this.measurementName=measurementName;
        //this.measurementName="B1"+file_name.substring(file_name.length()-7,file_name.length()-4);
        //Query query=new Query("select * from B513 where  \"Date\"="+takeofftime+"and \"FLIGHT NUMBER\"="+measurementName,influxDBTemplate.getDatabase());
        //String result=influx.query(query).toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long systime= 0;
        try {
            systime = sdf.parse(takeofftime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long series_num = 0;
       //long systime = System.currentTimeMillis();
        try {
            CsvReader products = new CsvReader(file_path+file_name);
            products.readHeaders();
            String[] keys = products.getHeaders();
            products.skipRecord();
            products.skipRecord();
            int headersCount = products.getHeaderCount();
            String[] tag_keys=new String[7];
            String[] field_keys=new String[headersCount-7];
            String[] tag_values = new String[7];
            Double[] field_values = new Double[headersCount - 7];
            //Double[] field_values_sum=new Double[headersCount-7];
            for (int headersSeries = 0; headersSeries < 7; headersSeries = headersSeries + 1) {
                tag_keys[headersSeries] = keys[headersSeries];
            }
            for (int headersSeries = 0; headersSeries < headersCount - 7; headersSeries = headersSeries + 1) {

                field_keys[headersSeries] = keys[headersSeries+7];
            }
            BatchPoints batchPoints = BatchPoints
                    .database(dbname)
                    .retentionPolicy(rpName)
                    .consistency(InfluxDB.ConsistencyLevel.ALL)
                    .build();
            Map<String, Double> mapDouble = new HashMap<String, Double>(headersCount-7);
            Map<String, Object> mapObject = new HashMap<String, Object>();
            Map<String,String> mapTAG=new HashMap<String, String>();
            while (products.readRecord()) {


                for (int TagSeries=0;TagSeries<7;TagSeries=TagSeries+1) {
                    String tag_value = products.get(TagSeries);
                    if (tag_value != null && tag_value.length() > 0) {
                        tag_values[TagSeries] = tag_value;
                    } else {
                        tag_values[TagSeries] = "null";
                    }
                    mapTAG.put(tag_keys[TagSeries],tag_values[TagSeries]);
                }
                for (int FieldSeries = 0; FieldSeries < headersCount - 7; FieldSeries = FieldSeries + 1) {
                    //int headersSeries=0;
                    String field_value = products.get(FieldSeries + 7);
                    if (field_value != null && field_value.length() > 0) {
                        field_values[FieldSeries] = Double.parseDouble(field_value);
                    } else {
                        field_values[FieldSeries] = null;
                    }
                    //field_values_sum[FieldSeries]=field_values_sum[FieldSeries]+field_values[FieldSeries];
                    mapDouble.put(field_keys[FieldSeries],field_values[FieldSeries]);
                }
                mapObject.putAll(mapDouble);
//                    for (Map.Entry<String, Double> entry : mapDouble.entrySet()) {
//                        if (entry.getValue() instanceof java.lang.Object) {
//                            mapObject.put(entry.getKey(), (java.lang.Object) entry.getValue());
//                        }
//                    }
                Point point = Point.measurement(measurementName)
                        .time(5000 * series_num + systime, TimeUnit.MILLISECONDS)
                        .addField("Date",takeoffday1)
                        .fields(mapObject)
                        .tag("DATE",takeoffday)
                        .tag(mapTAG)
                        .build();
                batchPoints.point(point);
                series_num = series_num + 1;


            }
            influx.write(batchPoints);
            //Map<String,Double>mapmean=new HashMap<>(headersCount-7);
            for(int FieldSeries = 0; FieldSeries < headersCount - 7; FieldSeries = FieldSeries + 1){

            }
            products.close();
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            return "found no file";
        } catch (IOException e) {
            //e.printStackTrace();
            return "cant be written";
        }
        //Query query = new Query("SHOW TAG KEYS FROM flight11",influxDBTemplate.getDatabase());
        Query querymean = new Query("select mean(*) into mean from "+measurementName+" WHERE \"DATE\"='"+takeoffday+
                 "' group by \"DATE\" ,\"AIRCRAFT REGISTRATION\",\"DEPARTURE AIRPORT- ICAO\",\"ARRIVAL AIRPORT- ICAO\",\"FLIGHT NUMBER\""
                 ,dbname);
        QueryResult meanresult=influx.query(querymean);
        Query querymedian = new Query("select median(*) into median from "+measurementName+" WHERE \"DATE\"='"+takeoffday+
                "' group by \"DATE\" ,\"AIRCRAFT REGISTRATION\",\"DEPARTURE AIRPORT- ICAO\",\"ARRIVAL AIRPORT- ICAO\",\"FLIGHT NUMBER\""
                ,dbname);
        QueryResult medianresult=influx.query(querymedian);
        Query querymax = new Query("select max(*) into max from "+measurementName+" WHERE \"DATE\"='"+takeoffday+
                "' group by \"DATE\" ,\"AIRCRAFT REGISTRATION\",\"DEPARTURE AIRPORT- ICAO\",\"ARRIVAL AIRPORT- ICAO\",\"FLIGHT NUMBER\""
                ,dbname);
        QueryResult maxresult=influx.query(querymax);
        Query querymin = new Query("select min(*) into min from "+measurementName+" WHERE \"DATE\"='"+takeoffday+
                "' group by \"DATE\" ,\"AIRCRAFT REGISTRATION\",\"DEPARTURE AIRPORT- ICAO\",\"ARRIVAL AIRPORT- ICAO\",\"FLIGHT NUMBER\""
                ,dbname);
        QueryResult minresult=influx.query(querymin);
        return "written successfully";


    }
}



