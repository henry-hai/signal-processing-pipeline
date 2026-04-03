import java.io.*;

/**
 * Base class for a concurrent filter stage. Each filter runs on its own thread and
 * exchanges data with neighbors through piped streams. Subclasses implement {@link #run()}.
 *
 * <p>Upstream attribution: see {@code NOTICE} in the repository root.
 */
public class FilterFramework extends Thread {

    private final PipedInputStream inputReadPort = new PipedInputStream();
    private final PipedOutputStream outputWritePort = new PipedOutputStream();
    private FilterFramework inputFilter;

    class EndOfStreamException extends Exception {
        EndOfStreamException() {
            super();
        }

        EndOfStreamException(String s) {
            super(s);
        }
    }

    /** Connect this filter's input to the upstream filter's output pipe. */
    void Connect(FilterFramework upstream) {
        try {
            inputReadPort.connect(upstream.outputWritePort);
            inputFilter = upstream;
        } catch (Exception e) {
            System.out.println("\n" + getName() + " FilterFramework error connecting::" + e);
        }
    }

    /**
     * Read one byte from the input port, waiting if necessary until data is available
     * or the upstream filter has finished.
     */
    byte ReadFilterInputPort() throws EndOfStreamException {
        byte datum = 0;

        try {
            while (inputReadPort.available() == 0) {
                if (EndOfInputStream()) {
                    throw new EndOfStreamException("End of input stream reached");
                }
                sleep(250);
            }
        } catch (EndOfStreamException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("\n" + getName() + " Error in read port wait loop::" + e);
        }

        try {
            datum = (byte) inputReadPort.read();
            return datum;
        } catch (Exception e) {
            System.out.println("\n" + getName() + " Pipe read error::" + e);
            return datum;
        }
    }

    void WriteFilterOutputPort(byte datum) {
        try {
            outputWritePort.write(datum & 0xFF);
            outputWritePort.flush();
        } catch (Exception e) {
            System.out.println("\n" + getName() + " Pipe write error::" + e);
        }
    }

    /** True when the upstream filter thread is no longer alive. */
    private boolean EndOfInputStream() {
        return !inputFilter.isAlive();
    }

    void ClosePorts() {
        try {
            inputReadPort.close();
            outputWritePort.close();
        } catch (Exception e) {
            System.out.println("\n" + getName() + " ClosePorts error::" + e);
        }
    }

    @Override
    public void run() {
        // Override in concrete filters.
    }
}
