import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Terminal stage: frames the byte stream into 4-byte measurement IDs and 8-byte payloads,
 * interprets time stamps (ID 0) and logs measurement ID 4 (pressure) as doubles to stdout.
 */
public class SinkFilter extends FilterFramework {

    private static final int MEASUREMENT_LENGTH = 8;
    private static final int ID_LENGTH = 4;

    @Override
    public void run() {
        Calendar timeStamp = Calendar.getInstance();
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");

        byte dataByte;
        int bytesRead = 0;
        long measurement;
        int id;

        System.out.print("\n" + getName() + "::Sink reading ");

        while (true) {
            try {
                id = 0;
                for (int i = 0; i < ID_LENGTH; i++) {
                    dataByte = ReadFilterInputPort();
                    id |= (dataByte & 0xFF);
                    if (i != ID_LENGTH - 1) {
                        id <<= 8;
                    }
                    bytesRead++;
                }

                measurement = 0;
                for (int i = 0; i < MEASUREMENT_LENGTH; i++) {
                    dataByte = ReadFilterInputPort();
                    measurement |= (dataByte & 0xFFL);
                    if (i != MEASUREMENT_LENGTH - 1) {
                        measurement <<= 8;
                    }
                    bytesRead++;
                }

                if (id == 0) {
                    timeStamp.setTimeInMillis(measurement);
                }

                if (id == 4) {
                    System.out.print(timeStampFormat.format(timeStamp.getTime()) + " ID = " + id + " "
                            + Double.longBitsToDouble(measurement));
                }
                System.out.print("\n");
            } catch (EndOfStreamException e) {
                ClosePorts();
                System.out.print("\n" + getName() + "::Sink exiting; bytes read: " + bytesRead);
                break;
            }
        }
    }
}
