package cop.swing.controls.sections;

import org.apache.commons.collections.CollectionUtils;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
final class SectionContainer<S extends Section> implements ComponentListener, ChangeListener {
    public static final int UNLIMITED = 0;

    private final Rectangle cmpBounds = new Rectangle();
    private final Rectangle bounds = new Rectangle();
    private final Point point = new Point();

    private final List<S> sections = new ArrayList<S>();
    private final SectionViewer<S> viewer;
    private final int maxSections;

    public SectionContainer(SectionViewer<S> viewer, int maxSections) {
        this.maxSections = maxSections > UNLIMITED ? maxSections : Integer.MAX_VALUE;
        this.viewer = viewer;
    }

    public int size() {
        return sections.size();
    }

    public List<S> getSections() {
        return sections.isEmpty() ? Collections.<S>emptyList() : Collections.unmodifiableList(sections);
    }

    public int getSectionPosition(S section) {
        return sections.indexOf(section);
    }

    public void move(int index, S section) {
        if (!sections.contains(section))
            return;

        SectionViewer<? extends Section> viewer = section.getViewer();

        try {
            sections.remove(section);
            sections.add(index, section);
            section.setViewer((SectionViewer<Section>)this.viewer);
        } catch(Exception ignored) {
            section.setViewer(viewer);
        }
    }

    public int getMaxSections() {
        return maxSections;
    }

    public void add(S section) {
        if (sections.size() >= maxSections)
            return;

        section.setViewer((SectionViewer<S>)viewer);
        sections.add(section);
    }

    public void add(int index, S section) {
        if (sections.size() >= maxSections)
            return;

        SectionViewer<? extends Section> viewer = section.getViewer();

        try {
            sections.add(index, section);
            section.setViewer((SectionViewer<S>)this.viewer);
        } catch(Exception ignored) {
            section.setViewer(viewer);
        }
    }

    public void addAll(Collection<S> sections) {
        if (CollectionUtils.isNotEmpty(sections))
            for (S section : sections)
                add(section);
    }

    public S get(int index) {
        return sections.size() > index ? sections.get(index) : null;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public boolean isFull() {
        return sections.size() >= maxSections;
    }

    public boolean remove(S section) {
        return sections.remove(section);
    }

    public void setBackground(Color color) {
        for (S section : sections)
            section.setBackground(color);
    }

    public void clear() {
        sections.clear();
    }

//    private void updateVisibleSections(Component comp) {
//        comp.getBounds(cmpBounds);
//
//        for (S section : sections) {
//            SectionViewer.convertRect(section.getParent(), section.getBounds(bounds), comp, point);
//
//            if (bounds.intersects(cmpBounds))
//                visibleSections.add(section);
//            else if (!visibleSections.isEmpty())
//                break;
//        }
//    }

    // ========== ComponentListener ==========

    @Override
    public void componentResized(ComponentEvent event) {
        Component comp = event.getComponent();

//        if (!(comp instanceof JScrollPane))
//            updateVisibleSections(comp);
    }

    @Override
    public void componentMoved(ComponentEvent event) {
    }

    @Override
    public void componentShown(ComponentEvent event) {
    }

    @Override
    public void componentHidden(ComponentEvent event) {
    }

    // ========== ChangeListener ==========

    @Override
    public void stateChanged(ChangeEvent event) {
//        updateVisibleSections((Component)event.getSource());
    }
}
