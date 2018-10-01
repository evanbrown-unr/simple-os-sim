# Evan Brown
# Private repository for CS 446 coursework at UNR

## Operating System Simulator Project

This repository contains the simulation projects for CS 446 at UNR.
There will be four phases, each of which building off of the preceeding phase.
The essence of all of the phases is that a configuration file is read it to set up
the simulator. Then a metadata file is read to set up the PCBs within the OS queues.
After that, the process is ran with all processes\operations being logged to the monitor
and\or a logfile.

## Compilation and Running Instructions

### Compile

> make

### Running

> java OSSim <config_file_path>.conf


## Phase One
For the first phase, we are implementing an FCFS scheduling algorithm,
as it is the simplest to work with. After finishing the project to specification, I feel
like the design pattern I chose will help me when I add to or add more modules later on.
Obviously, with the nature of Java, the program has a very object oriented approach which
makes it easier to read, debug, and extend.

## Phase Two
For this phase we are still scheduling our tasks using FCFS. However, every input/output
operation must be run on a seperate thread. For now, we are not worrying about thread pooling,
but it's something to consider in the near future. Another change is that we are setting the process's
state inside of its PCB. Although this can't be seen on the user end, the logical flow is in the program.
This will help a lot when we come to implementing more complicated scheduling algorithms. The output
format for this phase has changed a lot, as we are now using a timestamp model to show when operations
start and end during the course of the simulation.

## Phase Three
TBD.
I've staged all the current modules into a directory.