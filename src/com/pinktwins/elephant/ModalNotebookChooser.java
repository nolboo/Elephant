package com.pinktwins.elephant;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import com.pinktwins.elephant.Notebooks.NotebookActionListener;
import com.pinktwins.elephant.data.Notebook;

public class ModalNotebookChooser extends JDialog {

	private static final long serialVersionUID = 4290404794842317473L;

	public static final int fixedWidth = 442;
	public static final int fixedHeight = 622;

	private Notebooks notebooks;

	private NotebookActionListener naListener;

	public void setNotebookActionListener(NotebookActionListener l) {
		naListener = l;
	}

	public ModalNotebookChooser(Frame owner, String title, boolean modal) {
		super(owner, title, modal);

		setUndecorated(true);
		setLayout(new BorderLayout());

		notebooks = new Notebooks((ElephantWindow) owner, true, title);
		add(notebooks);

		notebooks.search.requestFocusInWindow();

		notebooks.setNotebookActionListener(new NotebookActionListener() {
			@Override
			public void didCancelSelection() {
				close();
			}

			@Override
			public void didSelect(Notebook nb) {
				if (naListener != null) {
					naListener.didSelect(nb);
					close();
				}
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				notebooks.search.requestFocusInWindow();
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				close();
			}
		});
	}

	protected void close() {
		setVisible(false);
		dispose();
	}

	public void handleKeyEvent(KeyEvent e) {
		switch (e.getID()) {
		case KeyEvent.KEY_PRESSED:
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				close();
				break;
			case KeyEvent.VK_UP:
				// arrows move focus away from search anyway, this would be extra.
				if (!notebooks.search.hasFocus()) {
					notebooks.changeSelection(-1, e.getKeyCode());
				}
				break;
			case KeyEvent.VK_DOWN:
				if (!notebooks.search.hasFocus()) {
					notebooks.changeSelection(1, e.getKeyCode());
				}
				break;
			default:
				notebooks.handleKeyEvent(e);
			}
			break;
		}
	}
}