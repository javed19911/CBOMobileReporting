package services;

/**
 * Created by Kuldeep.Dwivedi on 12/1/2014.
 */
public interface TaskListener<T>
{
    void onStarted();
    void onFinished(T result);
}
