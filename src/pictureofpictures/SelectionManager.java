package pictureofpictures;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;

public class SelectionManager {
    
    private JPanel source;
    protected int lastSelectedIndex;
    
    public SelectionManager(JPanel source) {
        this.source = source;
    }
    
    protected Component[] getSelectedEntries() {
        List<Component> selectedEntries = new ArrayList<>();
        for (int i = 0; i < source.getComponentCount(); i++) {
            if (source.getComponent(i) instanceof PictureSelectionEntry) {
                PictureSelectionEntry temp = (PictureSelectionEntry) source.getComponent(i);
                if (temp.selected) 
                    selectedEntries.add(source.getComponent(i));
            }
        }
        return selectedEntries.toArray(new Component[selectedEntries.size()]);
    }
    
    protected Integer[] getSelectedIndecies() {
        List<Integer> selectedIndecies = new ArrayList<>();
        for (int i = 0; i < source.getComponentCount(); i++) {
            if (source.getComponent(i) instanceof PictureSelectionEntry) {
                PictureSelectionEntry temp = (PictureSelectionEntry) source.getComponent(i);
                if (temp.selected) 
                    selectedIndecies.add(i);
            }
        }
        return selectedIndecies.toArray(new Integer[selectedIndecies.size()]);
    }
    
    /**
     * Removed the selected entries from the source
     * @return The number of entries removed
     */
    protected int removeSelectedFromSource() {
        Integer[] selected = this.getSelectedIndecies();
        int removed = selected.length;
        
        while (selected.length > 0) {
            this.source.remove(selected[0]);
            selected = this.getSelectedIndecies(); //update selected list
        }
        return removed;
    }
    
    protected void removeAdjacentSeperatorsFromSource() {
        //remove from front
        while (this.source.getComponentCount() > 0
                && this.source.getComponent(0) instanceof HorizontalSeperator)
            this.source.remove(0);
        
        //remove from back
        while (this.source.getComponentCount() > 0
                && this.source.getComponent(this.source.getComponentCount() - 1) instanceof HorizontalSeperator)
            this.source.remove(this.source.getComponentCount() - 1);
        
        //remove from middle
        for (int i = 0; i < this.source.getComponentCount(); i++) {
            if (i+1< this.source.getComponentCount()) {
                if (this.source.getComponent(i) instanceof HorizontalSeperator
                        && this.source.getComponent(i+1) instanceof HorizontalSeperator) {
                    this.source.remove(i+1);
                    i--;
                }
                
            }
        }
    }
    
    protected PictureSelectionEntry[] getAllEntries() {
        List<PictureSelectionEntry> entries = new ArrayList<>();
        for (int i = 0; i < source.getComponentCount(); i++) {
            if (source.getComponent(i) instanceof PictureSelectionEntry) {
                PictureSelectionEntry temp = (PictureSelectionEntry) source.getComponent(i);
                entries.add(temp);
                //System.out.println("equal references: " + (source.getComponent(i) == temp)); //should return true WHICH IS AMAZINGLY USEFUL
            }
        }
        return entries.toArray(new PictureSelectionEntry[entries.size()]);
    }
    
    /**
     * Counts the total number of entries
     * @return Returns the total number of entries
     */
    protected int countNumEntries() {
        int count = 0;
        for (int i = 0; i < source.getComponentCount(); i++) {
            if (source.getComponent(i) instanceof PictureSelectionEntry)                
                count++;
        }
        return count;
    }
    
    /**
     * Counts the total number of enabled entries
     * @return Returns the number of enabled entries
     */
    protected int countNonDisabledEntries() {
        int count = 0;
        for (int i = 0; i < source.getComponentCount(); i++) {
            if (source.getComponent(i) instanceof PictureSelectionEntry) {
                PictureSelectionEntry temp = (PictureSelectionEntry) source.getComponent(i);
                if (temp.isUserEnabled())
                    count++;
            }
                
        }
        return count;
    }
    
    protected List<SpecialPicture> getAllSpecialPictures() {
        List<SpecialPicture> pictures = new ArrayList<>();
        for (int i = 0; i < source.getComponentCount(); i++) {
            if (source.getComponent(i) instanceof PictureSelectionEntry) {
                PictureSelectionEntry temp = (PictureSelectionEntry) source.getComponent(i);
                pictures.add(temp.picture);
                //System.out.println("equal references: " + (source.getComponent(i) == temp)); //should return true WHICH IS AMAZINGLY USEFUL
            }
        }
        return pictures;
    }
    
    protected List<SpecialPicture> getAllEnabledSpecialPictures() {
        List<SpecialPicture> pictures = new ArrayList<>();
        for (int i = 0; i < source.getComponentCount(); i++) {
            if (source.getComponent(i) instanceof PictureSelectionEntry) {
                PictureSelectionEntry temp = (PictureSelectionEntry) source.getComponent(i);
                
                if (temp.isUserEnabled())
                    pictures.add(temp.picture);
                //System.out.println("equal references: " + (source.getComponent(i) == temp)); //should return true WHICH IS AMAZINGLY USEFUL
            }
        }
        return pictures;
    }
    
    protected int indexOf(PictureSelectionEntry entry) {
        PictureSelectionEntry[] entries = this.getAllEntries();
        for (int i = 0; i < entries.length; i++) {
            if (entries[i] == entry)
                return i;
        }
        return -1;
    }
    
    protected void shiftSelect(PictureSelectionEntry entry) {
        PictureSelectionEntry[] entries = this.getAllEntries();
        int entryIndex = this.indexOf(entry);
        
        System.out.println("Last selected: " + this.lastSelectedIndex + "  to: " + entryIndex);
        
        int from;
        int to;
        if (entryIndex > this.lastSelectedIndex) {
            from = this.lastSelectedIndex;
            to = entryIndex;
        } else if (entryIndex < this.lastSelectedIndex) {
            from = entryIndex;
            to = this.lastSelectedIndex;
        } else {
            return;
        }
        
        System.out.println("from: " + from + "  to: " + to);
        
        for (int i = from; i <= to; i++)
            entries[i].selected = true;
        
        source.repaint();   //repaint all of the entries
    }
    
    protected int getNumEntries() {
        return this.getAllEntries().length;
    }
}
