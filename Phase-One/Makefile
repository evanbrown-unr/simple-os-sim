JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        OSSim.java \
        OperatingSystem.java \
        Configuration.java \
        Logger.java \
        Process.java \
        BasicTimer.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class