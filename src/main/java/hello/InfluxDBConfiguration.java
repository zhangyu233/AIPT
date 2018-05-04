package hello;
import org.influxdb.dto.*;
import org.springframework.data.influxdb.converter.PointConverter;
import org.springframework.web.bind.*;
import org.springframework.context.annotation.*;
import org.springframework.boot.context.properties.*;
import org.springframework.data.influxdb.*;
    @Configuration
    @EnableConfigurationProperties(InfluxDBProperties.class)
    public class InfluxDBConfiguration
    {
        @Bean
        public InfluxDBConnectionFactory connectionFactory(final InfluxDBProperties properties)
        {
            return new InfluxDBConnectionFactory(properties);
        }

        @Bean
        public InfluxDBTemplate<Point> influxDBTemplate(final InfluxDBConnectionFactory connectionFactory)
        {
            /*
             * You can use your own 'PointCollectionConverter' implementation, e.g. in case
             * you want to use your own custom measurement object.
             */
            return new InfluxDBTemplate<>(connectionFactory, new PointConverter());
        }

        @Bean
        public DefaultInfluxDBTemplate defaultTemplate(final InfluxDBConnectionFactory connectionFactory)
        {
            /*
             * If you are just dealing with Point objects from 'influxdb-java' you could
             * also use an instance of class DefaultInfluxDBTemplate.
             */
            return new DefaultInfluxDBTemplate(connectionFactory);
        }
    }

