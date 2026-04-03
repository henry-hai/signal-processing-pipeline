/**
 * Wires the pipeline (source → middle → sink), connects piped ports, and starts all filter threads.
 * Entry point for the demo.
 */
public class Plumber {

    public static void main(String[] args) {
        SourceFilter source = new SourceFilter();
        MiddleFilter middle = new MiddleFilter();
        SinkFilter sink = new SinkFilter();

        // Connect from sink backward: each filter's input attaches to the upstream output pipe.
        sink.Connect(middle);
        middle.Connect(source);

        source.start();
        middle.start();
        sink.start();
    }
}
