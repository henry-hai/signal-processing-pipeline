# Signal processing data pipeline

Small **pipe-and-filter** demo in Java: a source reads a binary flight-style instrumentation stream, an intermediate stage forwards bytes (a natural place to add validation or transforms), and a sink reframes records and prints selected measurements to the console.

## Layout

```text
src/
  FilterFramework.java   # threaded stage base class (piped I/O)
  SourceFilter.java      # reads data/FlightData.dat
  MiddleFilter.java      # pass-through (extend for your own stages)
  SinkFilter.java        # parses ID + 8-byte measurement words; logs ID 4 with timestamps
  Plumber.java           # main — wires and starts the three filters
data/
  README.md              # where to put FlightData.dat
```

## Data

Add **`data/FlightData.dat`** (binary stream expected by the source). It is not included in the repo. See `data/README.md`.

## Build and run

From the repository root (so `data/FlightData.dat` resolves correctly):

```bash
mkdir -p out
javac -encoding UTF-8 -d out src/*.java
java -cp out Plumber
```

On Windows PowerShell:

```powershell
New-Item -ItemType Directory -Force -Path out | Out-Null
javac -encoding UTF-8 -d out src/*.java
java -cp out Plumber
```

Requires **JDK 8+** (tested with modern JDKs; no build tool required).

## Architecture

```text
[ SourceFilter ] → [ MiddleFilter ] → [ SinkFilter → stdout ]
```

Each arrow is a `PipedOutputStream` / `PipedInputStream` pair. Filters run as separate threads; the framework waits on the input pipe and detects end-of-stream when the upstream thread terminates.

## Extending

- Add stages by subclassing `FilterFramework` and chaining them from `Plumber`.
- Implement CSV export, anomaly handling, or extra measurements in `MiddleFilter` / `SinkFilter` (or additional sinks).

## License and attribution

This project is licensed under the [MIT License](LICENSE). The pipe-and-filter framework derives from instructional materials; see [NOTICE](NOTICE).
