package io.prometheus.client.exporter;

import io.prometheus.client.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ExampleExporter {

  static final Gauge g = Gauge.build().name("gauge").help("blah").register();
  static final Counter c = Counter.build().name("counter").help("meh").register();
  static final Summary s = Summary.build().name("summary").help("meh").register();
  static final Histogram h = Histogram.build().name("histogram").help("meh").register();
  static final Gauge l = Gauge.build().name("labels").help("blah").labelNames("l").register();

  public static void main(String[] args) throws Exception {
    Collector cc = new YourCustomCollector();
    cc.register();

    Server server = new Server(1235);
    ServletContextHandler context = new ServletContextHandler();
    context.setContextPath("/");
    server.setHandler(context);
    context.addServlet(new ServletHolder(new MetricsServlet()), "/metrics");
    g.set(1);
    c.inc(2);
    s.observe(3);
    h.observe(4);
    l.labels("foo").inc(5);
    server.start();
    server.join();
  }

  public static class YourCustomCollector extends Collector {
    public List<MetricFamilySamples> collect() {
      List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
      // With no labels.
      mfs.add(new CounterMetricFamily("my_counter", "help", 42));

      SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String time="2019-09-5 16:39:15";
      Date date = null;
      try {
        date = format.parse(time);
      } catch (ParseException e) {
        e.printStackTrace();
      }

      long ts = date.getTime();
      // With labels
      CounterMetricFamily labeledCounter = new CounterMetricFamily("my_other_counter", "help", Arrays.asList("labelname"));
      labeledCounter.addMetric(Arrays.asList("foo"), 4, ts);
      labeledCounter.addMetric(Arrays.asList("bar"), 5, ts);
      labeledCounter.addMetric(Arrays.asList("bar"), 5, ts);
      mfs.add(labeledCounter);
      return mfs;
    }
  }

}
