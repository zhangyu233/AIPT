package hello;
import org.springframework.data.influxdb.*;
public class WriteBoolen {
    InfluxDBTemplate influxDBTemplate;
    String measurement;
    String file_path;
    WriteCSV writecsv=new WriteCSV();
    public WriteBoolen(InfluxDBTemplate influxDBTemplate,String measurementname,String file_path,String file_name){
        String writeboolen = writecsv.WriteCSV(influxDBTemplate, measurementname,file_path,file_name);
    }
}
