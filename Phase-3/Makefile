JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = OSSim.java \
		  OperatingSystem.java \
		  ProcessControlBlock.java \
		  Logger.java \
		  Configuration.java \
		  Operation.java \
		  Memory.java \
		  ResourceManager.java \
		  BasicTimer.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class