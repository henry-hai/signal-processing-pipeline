/**
 * Intermediate pass-through stage: forwards each byte from input to output unchanged.
 * Replace or extend this class to add parsing, validation, or transformation in the pipeline.
 */
public class MiddleFilter extends FilterFramework {

    @Override
    public void run() {
        int bytesRead = 0;
        int bytesWritten = 0;
        byte dataByte;

        System.out.print("\n" + getName() + "::Middle reading ");

        while (true) {
            try {
                dataByte = ReadFilterInputPort();
                bytesRead++;
                WriteFilterOutputPort(dataByte);
                bytesWritten++;
            } catch (EndOfStreamException e) {
                ClosePorts();
                System.out.print("\n" + getName() + "::Middle exiting; bytes read: " + bytesRead
                        + " bytes written: " + bytesWritten);
                break;
            }
        }
    }
}
