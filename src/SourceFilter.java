import java.io.*;

/**
 * Source stage: reads a binary instrumentation stream from disk and pushes bytes to the output pipe.
 * Default path is {@code data/FlightData.dat} relative to the process working directory (repository root).
 */
public class SourceFilter extends FilterFramework {

    @Override
    public void run() {
        String fileName = "data/FlightData.dat";
        int bytesRead = 0;
        int bytesWritten = 0;
        DataInputStream in = null;
        byte dataByte;

        try {
            in = new DataInputStream(new FileInputStream(fileName));
            System.out.println("\n" + getName() + "::Source reading file...");

            while (true) {
                dataByte = in.readByte();
                bytesRead++;
                WriteFilterOutputPort(dataByte);
                bytesWritten++;
            }
        } catch (EOFException e) {
            System.out.println("\n" + getName() + "::End of file reached...");
            try {
                if (in != null) {
                    in.close();
                }
                ClosePorts();
                System.out.println("\n" + getName() + "::Read complete; bytes read: " + bytesRead
                        + " bytes written: " + bytesWritten);
            } catch (Exception closeErr) {
                System.out.println("\n" + getName() + "::Error closing input file::" + closeErr);
            }
        } catch (IOException e) {
            System.out.println("\n" + getName() + "::Problem reading input data file::" + e);
        }
    }
}
