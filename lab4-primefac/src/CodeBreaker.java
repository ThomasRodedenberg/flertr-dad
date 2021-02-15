

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import client.view.ProgressItem;
import client.view.StatusWindow;
import client.view.WorklistItem;
import network.Sniffer;
import network.SnifferCallback;
import rsa.Factorizer;
import rsa.ProgressTracker;

public class CodeBreaker implements SnifferCallback {

	public final ExecutorService tp = Executors.newFixedThreadPool(2);
	private final JPanel workList;
	private final JPanel progressList;

	private final JProgressBar mainProgressBar; 


	// -----------------------------------------------------------------------

	private CodeBreaker() {
		StatusWindow w  = new StatusWindow();

		workList        = w.getWorkList();
		progressList    = w.getProgressList();
		mainProgressBar = w.getProgressBar();

		mainProgressBar.setStringPainted(true);
		mainProgressBar.setValue(0);

		w.enableErrorChecks();
		new Sniffer(this).start();
	}

	// -----------------------------------------------------------------------

	public static void main(String[] args) throws Exception {

		/*
		 * Most Swing operations (such as creating view elements) must be
		 * performed in the Swing EDT (Event Dispatch Thread).
		 * 
		 * That's what SwingUtilities.invokeLater is for.
		 */

		SwingUtilities.invokeLater(() -> new CodeBreaker());

	}

	// -----------------------------------------------------------------------

	/** Called by a Sniffer thread when an encrypted message is obtained. */
	@Override
	public void onMessageIntercepted(String message, BigInteger n) {
		System.out.println("message intercepted (N=" + n + ")...");

		SwingUtilities.invokeLater(() -> {
			WorklistItem wLI = new WorklistItem(n,message);
			ProgressItem pI = new ProgressItem(n,message);
			SwingUtilities.invokeLater(() -> workList.add(wLI));

			wLI.getBreakButton().addActionListener(e -> {
				SwingUtilities.invokeLater(() -> {
					workList.remove(wLI);
					progressList.add(pI);
					mainProgressBar.setMaximum(mainProgressBar.getMaximum() + 1000000);});
				tp.submit(() -> threadCrack(message,n,pI));
			});

			pI.getRemoveButton().addActionListener(e -> {
				SwingUtilities.invokeLater(() -> progressList.remove(pI));});

		});
	}
	private void threadCrack(String message, BigInteger n, ProgressItem pI) {
		ProgressTracker tracker = new Tracker(pI);


		String med = Factorizer.crack(message, n, tracker);
		SwingUtilities.invokeLater(() -> {
			pI.getTextArea().setText(med);
			pI.addRemoveButton();
			mainProgressBar.setValue(mainProgressBar.getValue() - 1000000);
			mainProgressBar.setMaximum(mainProgressBar.getMaximum() - 1000000);});

	}
	private class Tracker implements ProgressTracker {
		private int totalProgress = 0;
		private int prevProg = -1;
		private ProgressItem pI;

		private Tracker(ProgressItem pI) {
			this.pI = pI;
		}

		@Override
		public void onProgress(int ppmDelta) {
			totalProgress += ppmDelta;
			if (totalProgress != prevProg) {
				SwingUtilities.invokeLater(() -> {          		
					pI.getProgressBar().setValue(totalProgress);
					mainProgressBar.setValue(mainProgressBar.getValue() + ppmDelta);});
				prevProg = totalProgress;

			}
		}
	}

}
