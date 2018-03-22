/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SimBuilderTools;

import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Arlo
 */
public abstract class AbstractCreationPanel<T> extends JPanel
{

    ArrayList<CreatorListener> listeners;
    boolean isDeleted;

    public AbstractCreationPanel()
    {
        this.listeners = new ArrayList<>();
        isDeleted = false;
    }

    public void addListener(CreatorListener newListener)
    {
        this.listeners.add(newListener);
    }

    public boolean isDeleted()
    {
        return isDeleted;
    }
    
    

    public abstract void Save();

    public abstract void Load();

    public abstract boolean Validate();

    public abstract T buildObject();

    public abstract void setObject(T object);

    public void notifyListeners()
    {
        for (CreatorListener listener : this.listeners)
        {
            listener.updateGUI();
        }
    }
}
