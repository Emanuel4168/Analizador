package main.java.analizador;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class Main extends JFrame implements ActionListener {

	public static JTextArea area, consola;
	private JButton btnCompilar,btnVerTabla, btnAbrir, btnCerrar;
	private ArrayList<ValoresTabla> tablaSimbolos;
	
	public Main() {
		hazInterfaz();
	}
	
	
	public static void main(String[] args) throws UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(new NimbusLookAndFeel());
		new Main();
	}

	private void hazInterfaz() {
		setLayout(null);
		setUndecorated(true);
		setSize(500, 700);
		setLocationRelativeTo(null);
		setShape(new RoundRectangle2D.Double(0, 0, 500, 700, 20, 20));

		PanelGradiente panel = new PanelGradiente(	);
		panel.setBounds(0,0,500,700);
		
		area = new JTextArea();
		consola = new JTextArea();
		consola.setEnabled(false);
		consola.setDisabledTextColor(Color.BLACK);
		btnCompilar= new JButton("Compilar");
		btnCompilar.setBounds(30, 5, 150, 40);
		btnCompilar.addActionListener(this);
		btnVerTabla = new JButton("Tabla de simbolos");
		btnVerTabla.setBounds(175, 5, 150, 40);
		btnVerTabla.addActionListener(this);
		btnAbrir= new JButton("Abrir archivo");
		btnAbrir.setBounds(320, 5, 150, 40);
		btnAbrir.addActionListener(this);
		btnCerrar= new JButton("X");
		btnCerrar.setBounds(460,5, 40,40);
		btnCerrar.setBackground(Color.decode("#65417A"));
		btnCerrar.addActionListener(this);
		
		JScrollPane scrollPaneArea = new JScrollPane(area);
		scrollPaneArea.setBounds(30, 50, 440, 300);
		JScrollPane scrollPaneConsola = new JScrollPane(consola);
		scrollPaneConsola.setBounds(30, 350, 440, 330);
		
		add(scrollPaneArea);
		add(scrollPaneConsola);
		add(btnAbrir);
		add(btnCompilar);
		add(btnVerTabla);
		add(btnCerrar);
		add(panel);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==btnCerrar) 
			System.exit(0);
			
		
		if(e.getSource() == btnCompilar) {
			generarArchivo();
			compilar();
			return;
		}
		
		if(e.getSource() == btnVerTabla) {
			if(this.consola.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "No se ha realizado la compilacion");
				return;
			}
			TablaSimbolosVista viewTabla = new TablaSimbolosVista(this.tablaSimbolos);
			viewTabla.setSize(700, 460); 
			viewTabla.setLocationRelativeTo(null);
			viewTabla.setVisible(true);
			//viewTabla.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
			return;
		}
		
		JFileChooser chooser = new JFileChooser();
		int opcion = chooser.showSaveDialog(this);
		if (opcion == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();

			try {
				BufferedReader br= new BufferedReader(new FileReader(f));
				String lineaActual;
				while ((lineaActual = br.readLine()) != null) {
				    area.append(lineaActual+"\n");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		}
		
	}
	
	private void generarArchivo() {
		String ruta = "codigo.txt";
		File archivo = new File(ruta);
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(archivo));
			bw.write(area.getText());

			bw.close();
		} catch (Exception ex) {

		}
	}
	
	private void compilar() {		
		if(area.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(this, "Primero escribe c�digo...");
			area.requestFocus();
			return;
		}
		
		Analiza analizador = new Analiza("codigo.txt");
		ArrayList<String> a1 = analizador.resultado;
		ArrayList<Token> tk = analizador.tokenRC;
		Tabla t;
		Sintactico s;

		consola.setText("");
		
		for (int i = 0; i < a1.size(); i++) {
			consola.append(a1.get(i)+ "\n");
		}

		//if (a1.get(0).equals("No hay errores lexicos")) {
			s = new Sintactico(analizador.tokenRC);
			t = new Tabla(analizador.tokenRC);
			
			tablaSimbolos = t.getValoresTabla();
			Semantico semantic = new Semantico(tablaSimbolos,area.getText());
			ArrayList<String> semanticErrors = semantic.checkSemantic();
			consola.append("\n");
			for (int i = 0; i < semanticErrors.size(); i++) {
				consola.append(semanticErrors.get(i)+ "\n");
			}
		//}
		

	}
	

	class PanelGradiente extends JPanel {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setPaint(new GradientPaint(0, 0, Color.decode("#5A415B"), 500, 50, Color.decode("#65417A")));
			g2.fillRect(0, 0, getWidth(), 50);
			g2.setPaint(new GradientPaint(0, 0, Color.decode("#78415B"), 500, 700, Color.decode("#75417A")));
			g2.fillRect(0, 50, getWidth(), 670);
		}
	}

}
