JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        OSSim.java \
        OperatingSystem.java \
        Operation.java \
        Configuration.java \
        Logger.java \
        ProcessControlBlock.java \
        Timer.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class