# Operating System Simulator Project
# CS 446 UNR Coursework
# Evan Brown

## Repository Description
This repository contains the simulation projects for CS 446 at UNR. There will be four phases, each of which building off of the preceeding phase. The essence of all of the phases is that a configuration file is read it to set up the simulator. Then a metadata file is read to set up the PCBs within the OS queues. After that, the process is ran with all processes/operations being logged to the monitor and\or a logfile.

## Dependency Instructions

### Java
```bash
sudo apt-get install default-jdk
```
## Compilation and Running Instructions

### Compile
```bash
make
```
### Running
```bash
java OSSim <filepath>.conf
```
## Phase Descriptions

### Phase 1
For the first phase, we are implementing an FCFS scheduling algorithm, as it is the simplest to work with. After finishing the project to specification, I feel like the design pattern I chose will help me when I add to or add more modules later on. Obviously, with the nature of Java, the program has a very object oriented approach which makes it easier to read, debug, and extend.

### Phase 2
Please note that for this project the metadata operations for system and application processes has been changed from "begin"/"finish" to "start"/"end". The meta data files must be in this format or my simulator will throw the appropriate error. For this phase we are still scheduling our tasks using FCFS. However, every input/output operation must be run on a seperate thread. For now, we are not worrying about thread pooling, but it's something to consider in the near future. Another change is that we are setting the process's state inside of its PCB. Although this can't be seen on the user end, the logical flow is in the program. This will help a lot when we come to implementing more complicated scheduling algorithms. The output format for this phase has changed a lot, as we are now using a timestamp model to show when operations start and end during the course of the simulation.

### Phase 3
The main change for this project is the implementation of process synchronization techniques. We will be adding resource consraints into the configuration file that will limit the amount of resourecs a specific operation can own at a time. This will be done using semaphores and mutex locks. Another addition is to change the memory address generator to allocate memory in byte-sized blocks starting from 0x00000000, rather than generating a random 32-bit address. The method I chose to solve the synchronization problem was by using arrays of semaphores for each IO device, where each element contains a 1-user semaphore. This emulates the idea that only one user can access a given resource at any time. The critical section is defined by all of the code that is found in between acquiring a resource and releasing that resource. In my simulation, all of this is handled within the PCB module under the method that runs a given application.