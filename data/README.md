# Input data

Place a binary flight instrumentation stream named **`FlightData.dat`** in this directory (`data/`), **or** in the working directory from which you run `java` (see root `README.md`).

The bundled `SourceFilter` opens `FlightData.dat` by relative path. If you keep the file here, run the pipeline from `data/` or adjust the path in `SourceFilter` to match your layout.

The sample `.dat` file is not committed (size and course distribution). Obtain it from your course materials or generate a compatible stream for your own tests.
