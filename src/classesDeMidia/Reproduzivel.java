package classesDeMidia;
import java.util.List;

public interface Reproduzivel {
    void play();
    void pause();
    void stop();
    List<String> getTags();
}