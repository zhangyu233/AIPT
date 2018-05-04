package hello;

import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.influxdb.*;
import org.springframework.data.influxdb.InfluxDBTemplate;

import java.util.concurrent.TimeUnit;

@RestController
public class HelloController {
    @Autowired
    public InfluxDBTemplate<Point> influxDBTemplate;
    public String measurement = "flight";
    public String file_path = "/root/180205000424513.csv";

    //public void setInfluxDBTemplate(InfluxDBTemplate<Point> influxDBTemplate) {

    // }


    @RequestMapping("/")
    public String index() {
        return "Greetings from AIPT!";
    }


    @RequestMapping("/show")
    public ResponseEntity<QueryResult> get(@RequestParam(value = "command", defaultValue = "DATABASES") String command) {
        Query query = new Query("SHOW " + command, influxDBTemplate.getDatabase());
        QueryResult result = influxDBTemplate.query(query);
        return new ResponseEntity<QueryResult>(result, HttpStatus.OK);
    }


    @RequestMapping("/query_flight_number")
    public ResponseEntity<QueryResult> get(@RequestParam(value = "start_date", defaultValue = "") String start_date,
                                           @RequestParam(value = "end_date", defaultValue = "") String end_date,
                                           @RequestParam(value = "departure", defaultValue = "") String departure,
                                           @RequestParam(value = "arrival", defaultValue = "") String arrival){
        String query_flight_number_condition;
        if ((start_date == null || start_date.equals("")) && (end_date == null || end_date.equals(""))) {
            if (departure == null || departure.equals("")) {
                if (arrival == null || arrival.equals("")) {
                    query_flight_number_condition = "";
                } else {
                    query_flight_number_condition = " WHERE \"ARRIVAL AIRPORT- ICAO\"='" + arrival + "'";
                }
            } else {
                if (arrival == null || arrival.equals("")) {
                    query_flight_number_condition = " WHERE \"DEPARTURE AIRPORT- ICAO\"='" + departure + "'";
                } else {
                    query_flight_number_condition = " WHERE \"ARRIVAL AIRPORT- ICAO\"='" + arrival + "' AND \"DEPARTURE AIRPORT- ICAO\"='" + departure + "'";
                }
            }
        } else {
            if (departure == null || departure.equals("")) {
                if (arrival == null || arrival.equals("")) {
                    query_flight_number_condition = " WHERE mean_Date>=" + start_date + " AND mean_Date<=" + end_date;
                } else {
                    query_flight_number_condition = " WHERE mean_Date=" + start_date + " AND mean_Date<=" + end_date + " AND \"ARRIVAL AIRPORT- ICAO\"='" + arrival + "'";
                }
            } else {
                if (arrival == null || arrival.equals("")) {
                    query_flight_number_condition = " WHERE mean_Date>=" + start_date + " AND mean_Date<=" + end_date + " AND \"DEPARTURE AIRPORT- ICAO\"='" + departure + "'";
                } else {
                    query_flight_number_condition = " WHERE mean_Date>=" + start_date + " AND mean_Date<=" + end_date + " AND \"DEPARTURE AIRPORT- ICAO\"='" + departure + "'" + " AND \"ARRIVAL AIRPORT- ICAO\"='" + arrival + "'";
                }
            }
        }
        Query query = new Query("SELECT \"mean_Date\",\"AIRCRAFT REGISTRATION\",\"FLIGHT NUMBER\"" + " FROM mean "+query_flight_number_condition, influxDBTemplate.getDatabase());
        QueryResult result = influxDBTemplate.query(query);
        return new ResponseEntity<QueryResult>(result, HttpStatus.OK);
    }


    @RequestMapping("/query_sensor")
    public ResponseEntity<QueryResult> get(@RequestParam(value = "field_key1", defaultValue = "") String field_key1,
                                           @RequestParam(value = "field_key2", defaultValue = "") String field_key2,
                                           @RequestParam(value = "field_key3", defaultValue = "") String field_key3,
                                           @RequestParam(value = "field_key4", defaultValue = "") String field_key4,
                                           @RequestParam(value = "plane_number", defaultValue = "") String plane_number,
                                           @RequestParam(value = "flight_number", defaultValue = "") String flight_number,
                                           @RequestParam(value = "start_date", defaultValue = "") String start_date,
                                           @RequestParam(value = "end_date", defaultValue = "") String end_date) {
        String field_key;
        if (field_key1 == null || field_key1.equals("")) {
            if (field_key2 == null || field_key2.equals("")) {
                if (field_key3 == null || field_key3.equals("")) {
                    if (field_key4 == null || field_key4.equals("")) {
                        field_key = "*";
                    } else {
                        field_key = field_key4;
                    }
                } else {
                    if (field_key4 == null || field_key4.equals("")) {
                        field_key = field_key3;
                    } else {
                        field_key = field_key3 + "," + field_key4;
                    }
                }
            } else {
                if (field_key3 == null || field_key3.equals("")) {
                    if (field_key4 == null || field_key4.equals("")) {
                        field_key = field_key2;
                    } else {
                        field_key = field_key2 + "," + field_key4;
                    }
                } else {
                    if (field_key4 == null || field_key4.equals("")) {
                        field_key = field_key2 + "," + field_key3;
                    } else {
                        field_key = field_key2 + "," + field_key3 + "," + field_key4;
                    }
                }
            }
        } else {
            if (field_key2 == null || field_key2.equals("")) {
                if (field_key3 == null || field_key3.equals("")) {
                    if (field_key4 == null || field_key4.equals("")) {
                        field_key = field_key1;
                    } else {
                        field_key = field_key1 + "," + field_key4;
                    }
                } else {
                    if (field_key4 == null || field_key4.equals("")) {
                        field_key = field_key1 + "," + field_key3;
                    } else {
                        field_key = field_key1 + "," + field_key3 + "," + field_key4;
                    }
                }
            } else {
                if (field_key3 == null || field_key3.equals("")) {
                    if (field_key4 == null || field_key4.equals("")) {
                        field_key = field_key1 + "," + field_key2;
                    } else {
                        field_key = field_key1 + "," + field_key2 + "," + field_key4;
                    }
                } else {
                    if (field_key4 == null || field_key4.equals("")) {
                        field_key = field_key1 + "," + field_key2 + "," + field_key3;
                    } else {
                        field_key = field_key1 + "," + field_key2 + "," + field_key3 + "," + field_key4;
                    }
                }
            }
        }

        String query_sensor_condition;
        if ((start_date == null || start_date.equals(""))&&(end_date == null || end_date.equals(""))) {
            if(flight_number == null || flight_number.equals("")){
                query_sensor_condition=" WHERE \"AIRCRAFT REGISTRATION\"='"+plane_number+"'";
            }
            else{
                query_sensor_condition=" WHERE \"AIRCRAFT REGISTRATION\"='"+plane_number+"' AND \"FLIGHT NUMBER\"='"+flight_number+"'";
            }
        }
        else{
            if(flight_number == null || flight_number.equals("")){
                query_sensor_condition=" WHERE mean_Date>=" + start_date + " AND mean_Date<=" + end_date+" AND \"AIRCRAFT REGISTRATION\"='"+plane_number+"'";
            }
            else{
                query_sensor_condition=" WHERE mean_Date>=" + start_date + " AND mean_Date<=" + end_date+" AND \"AIRCRAFT REGISTRATION\"='"+plane_number+"' AND \"FLIGHT NUMBER\"='"+flight_number+"'";
            }
        }

        Query query = new Query("SELECT " + field_key + " FROM mean"+query_sensor_condition, influxDBTemplate.getDatabase());
        QueryResult result = influxDBTemplate.query(query);
        return new ResponseEntity<QueryResult>(result, HttpStatus.OK);
    }

    @RequestMapping("/write_data")
    public String writeboolen(@RequestParam(value = "measurementname", defaultValue = "flight") String measurementname,
                              @RequestParam(value = "filepath", defaultValue = "D:/proj/edition1/DH_data/") String filepath,
                              @RequestParam(value = "filename", defaultValue = "180305020140731.csv") String filename) {
        //WriteCSV writecsv=new WriteCSV();
        // String writeboolen=writecsv.WriteCSV(influxDBTemplate,measurement);
        String writeBoolen = new WriteCSV().WriteCSV(influxDBTemplate, measurementname,filepath, filename);
        return writeBoolen;
    }
}

