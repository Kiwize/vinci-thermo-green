/**
 * @author J�r�me Valenti
 */
package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import control.Controller;
import model.Mesure;

/**
 * ConsoleGUI : IHM de l'application de consultation des temp�ratures
 * Projet Vinci Thermo Green
 * @author J�r�me Valenti
 * @version 2.0.0
 * @see control.Controller
 * @see model.Mesure
 */
public class ConsoleGUI extends JFrame {

	private Controller control;
	/**
	 * Container interm�diaire JPanel
	 * Contient les crit�res de filtrage des donn�es de la table
	 * @see JPanel
	 */
	private JPanel criteriaPanel = new JPanel();

	/**
	 * Bouton radio pour le choix de conversion
	 */
	private JRadioButton rdbtnCelsius = new JRadioButton("Celsius");
	JRadioButton rdbtnFahrenheit = new JRadioButton("Fahrenheit");

	/**
	 * Liste de choix d'une zone</>
	 * @see JComboBox
	 */
	JComboBox<String> choixZone = new JComboBox<String>();

	/**
	 * Saisie de la date de d�but de p�riode
	 * @see JTextField
	 */
	private JTextField startDate;

	/**
	 * Saisie de la date de fin de p�riode
	 * @see JTextField
	 */
	private JTextField dateFin;

	private JButton btnFiltrer = new JButton("Filtrer");

	/**
	 * Container interm�diaire JPanel
	 * Contient l'affichage graphique des donn�es de la Table
	 * @see JPanel
	 */
	JPanel pnlParam = new JPanel();
	JPanel pnlGraph = new JPanel();

	/**
	 * Affiche la temp�rature minimale sur la p�riode
	 * @see JTextField
	 */
	private JTextField tempMin;

	/**
	 * Affiche la temp�rature moyenne sur la p�riode
	 * @see JTextField
	 */
	private JTextField tempMoy;

	/**
	 * Affiche la temp�rature maximale sur la p�riode
	 * @see JTextField
	 */
	private JTextField tempMax;

	/**
	 * Pour recevoir les donn�es collect�es
	 * @see JTable
	 */
	private JTable laTable;

	/**
	 * <p>
	 * Pour recevoir le JTable qui contient les mesures selectionn�es
	 * </p>
	 */
	private JScrollPane scrollPane = new JScrollPane();


	/**
	 * <p>
	 * Container interm�diaire JPanel
	 * </p>
	 * <p>
	 * Contient les bornes des valeurs nominales
	 * </p>
	 * 
	 * @see JPanel
	 */
	JPanel pnlBounds = new JPanel();

	public ConsoleGUI(Controller controller) throws ParseException {
		setIconImage(Toolkit.getDefaultToolkit().getImage("img/vinci_ico.jpg"));
		setTitle("Vinci Thermo Green");
		setSize(712, 510);
		setResizable(false);
		setFont(new Font("Consolas", Font.PLAIN, 12));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Pane pointe sur le container racine
		Container pane = getContentPane();
		// Fixe le Layout de la racine � Absolute
		getContentPane().setLayout(null);

		// D�finit le JPanel des crit�res
		criteriaPanel.setBounds(10, 10, 325, 145);
		criteriaPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Filtrage",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		criteriaPanel.setBackground(UIManager.getColor("Label.background"));
		criteriaPanel.setLayout(null);
		pane.add(criteriaPanel);

		// Ajoute deux boutons radio au JPanel pnlCriteria
		rdbtnCelsius.setFont(new Font("Consolas", Font.PLAIN, 12));
		rdbtnCelsius.setBounds(15, 20, 100, 23);
		criteriaPanel.add(rdbtnCelsius);

		// S�lectionne la convertion celsius par d�faut
		rdbtnCelsius.setSelected(true);

		rdbtnFahrenheit.setFont(new Font("Consolas", Font.PLAIN, 12));
		rdbtnFahrenheit.setBounds(115, 20, 100, 23);
		criteriaPanel.add(rdbtnFahrenheit);

		// Groupe les boutons radio.
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnCelsius);
		group.add(rdbtnFahrenheit);

		choixZone.setBounds(115, 50, 100, 20);
		criteriaPanel.add(choixZone);

		// un bouchon "Quick & Dirty" pour peupler la liste d�roulante
		// TODO peupler la liste avec un �quivalent � SELECT DISTINCT
		// TODO impl�menter la classe m�tier Zone pour peupler une JComboBox<Zone>
		choixZone.addItem("*");
		choixZone.addItem("01");
		choixZone.addItem("02");
		choixZone.addItem("03");
		choixZone.addItem("04");

		JLabel zoneLabel = new JLabel("Zone");
		zoneLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
		zoneLabel.setBounds(15, 54, 99, 14);
		criteriaPanel.add(zoneLabel);

		JLabel startLabel = new JLabel("D\u00E9but");
		startLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
		startLabel.setBounds(15, 83, 46, 14);
		criteriaPanel.add(startLabel);

		startDate = new JTextField();
		startDate.setBounds(115, 79, 100, 20);
		criteriaPanel.add(startDate);
		startDate.setColumns(10);

		JLabel endLabel = new JLabel("Fin");
		endLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
		endLabel.setBounds(15, 114, 46, 14);
		criteriaPanel.add(endLabel);

		dateFin = new JTextField();
		dateFin.setColumns(10);
		dateFin.setBounds(115, 110, 100, 20);
		criteriaPanel.add(dateFin);

		btnFiltrer.setBounds(225, 109, 89, 23);
		criteriaPanel.add(btnFiltrer);
		btnFiltrer.addActionListener(new filtrerData());

		JLabel vinciLogoLabel = new JLabel();
		vinciLogoLabel.setIcon(new ImageIcon("img/s_vinci.png"));
		vinciLogoLabel.setBounds(221, 11, 95, 35);
		criteriaPanel.add(vinciLogoLabel);

		// D�finit le JScrollPane qui re�oit la JTable
		scrollPane.setBounds(10, 160, 325, 310);
		pane.add(scrollPane);

		// D�finit le JPanel des param�tres du graphique
		pnlParam.setBounds(340, 10, 355, 335);
		pnlParam.setBorder(
				new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Graphique des temp\u00E9ratures",
						TitledBorder.LEADING, TitledBorder.TOP, null, new Color(128, 128, 128)));
		pnlParam.setBackground(UIManager.getColor("Label.background"));
		pnlParam.setLayout(null);
		pane.add(pnlParam);

		JCheckBox chckbxDistinctZone = new JCheckBox("Distinguer les zones");
		chckbxDistinctZone.setFont(new Font("Consolas", Font.PLAIN, 12));
		chckbxDistinctZone.setBounds(15, 20, 165, 23);
		pnlParam.add(chckbxDistinctZone);

		JLabel lblTypeDeGraphique = new JLabel("Type de graphique");
		lblTypeDeGraphique.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblTypeDeGraphique.setBounds(15, 50, 120, 14);
		pnlParam.add(lblTypeDeGraphique);

		JComboBox choixGraphique = new JComboBox();
		choixGraphique.setBounds(152, 47, 190, 20);
		pnlParam.add(choixGraphique);

		JButton refreshButton = new JButton("Actualiser");
		refreshButton.setBounds(222, 19, 120, 23);
		pnlParam.add(refreshButton);

		JLabel lblMin = new JLabel("Min");
		lblMin.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblMin.setBounds(15, 306, 30, 14);
		pnlParam.add(lblMin);

		tempMin = new JTextField();
		tempMin.setEditable(false);
		tempMin.setBounds(55, 302, 50, 20);
		pnlParam.add(tempMin);
		tempMin.setColumns(10);

		JLabel lblMoy = new JLabel("Moy");
		lblMoy.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblMoy.setBounds(137, 304, 30, 14);
		pnlParam.add(lblMoy);

		tempMoy = new JTextField();
		tempMoy.setEditable(false);
		tempMoy.setColumns(10);
		tempMoy.setBounds(177, 300, 50, 20);
		pnlParam.add(tempMoy);

		JLabel lblMax = new JLabel("Max");
		lblMax.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblMax.setBounds(252, 304, 30, 14);
		pnlParam.add(lblMax);

		tempMax = new JTextField();
		tempMax.setEditable(false);
		tempMax.setColumns(10);
		tempMax.setBounds(292, 300, 50, 20);
		pnlParam.add(tempMax);

		// D�finit le JPanel qui recoit le graphique
		pnlGraph.setBorder(new TitledBorder(null, "Graphique", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlGraph.setBackground(UIManager.getColor("Label.background"));
		pnlGraph.setBounds(15, 75, 330, 215);

		// pose le pnlGraph dans le pnlParam
		pnlParam.add(pnlGraph);
		pnlGraph.setLayout(null);

		// D�finit le JPanel des bornes nominales
		pnlBounds.setBounds(340, 346, 355, 124);
		pnlBounds.setBorder(new TitledBorder(null, "D\u00E9bord des valeurs nominales", TitledBorder.LEADING,
				TitledBorder.TOP, null, Color.GRAY));
		pnlBounds.setBackground(UIManager.getColor("Label.background"));
		pnlBounds.setLayout(null);
		pane.add(pnlBounds);

		JButton btnDebord = new JButton("D\u00E9bord");
		btnDebord.setBounds(266, 15, 79, 23);
		pnlBounds.add(btnDebord);

		JSlider slider = new JSlider();
		slider.setBounds(16, 40, 240, 25);
		pnlBounds.add(slider);

		JSlider slider_1 = new JSlider();
		slider_1.setBounds(15, 88, 240, 25);
		pnlBounds.add(slider_1);

		JLabel lblDebordMin = new JLabel("Minimum");
		lblDebordMin.setBounds(15, 20, 60, 14);
		pnlBounds.add(lblDebordMin);

		JLabel lblDebordMaximum = new JLabel("Maximum");
		lblDebordMaximum.setBounds(15, 70, 60, 14);
		pnlBounds.add(lblDebordMaximum);

		JLabel lbAlerte = new JLabel();
		lbAlerte.setIcon(new ImageIcon("img/s_green_button.png"));
		lbAlerte.setBounds(270, 42, 75, 75);
		pnlBounds.add(lbAlerte);
		
		//Old main func content
		
		this.setLocation(100, 100);
		control = controller;
		laTable = setTable(controller.getMesures());
		scrollPane.setViewportView(laTable);
		setChart();
	}

	/**
	 * <p>
	 * Transfert les donn�es de la collection vers un tableau d'objets
	 * </p>
	 * <p>
	 * La temp�rature est en degr� Fahrenheit
	 * </p>
	 * 
	 * @param ArrayList<Mesure>
	 * @return Object[][]
	 */
	private JTable setTable(ArrayList<Mesure> mesures) {

		Mesure mesure;
		
		float min = 0;
		float max = 0;
		float moy = 0;
		DecimalFormat round = new DecimalFormat("0.##");
		Object[][] dataTable = new Object[mesures.size()][3];

		if (rdbtnCelsius.isSelected()) {

			System.out.println("Celsius : " + rdbtnCelsius.isSelected() + " | " + mesures.size());

			// Initialisation de min et max
			min = mesures.get(0).getCelsius();
			max = mesures.get(0).getCelsius();

			for (int i = 0; i < mesures.size(); i++) {

				mesure = control.getMesures().get(i);
				dataTable[i][0] = mesure.getNumZone();
				dataTable[i][1] = mesure.getHoroDate();
				dataTable[i][2] = round.format(mesure.getCelsius());

				// Min, max et moy
				moy = moy + mesure.getCelsius();

				if (mesure.getCelsius() < min) {
					min = mesure.getCelsius();
				}
				if (mesure.getCelsius() > max) {
					max = mesure.getCelsius();
				}
			}
		} else {

			// Initialisation de min et max
			min = mesures.get(0).getFahrenheit();
			max = mesures.get(0).getFahrenheit();

			for (int i = 0; i < mesures.size(); i++) {
				mesure = control.getMesures().get(i);
				dataTable[i][0] = mesure.getNumZone();
				dataTable[i][1] = mesure.getHoroDate();
				dataTable[i][2] = round.format(mesure.getFahrenheit());

				// Min, max et moy
				moy = moy + mesure.getFahrenheit();

				if (mesure.getFahrenheit() < min) {
					min = mesure.getFahrenheit();
				}
				if (mesure.getCelsius() > max) {
					max = mesure.getFahrenheit();
				}
			}
		}

		String[] titreColonnes = { "Zone", "Date-heure", "T�" };
		JTable uneTable = new JTable(dataTable, titreColonnes);
		// Les donn�es de la JTable ne sont pas modifiables
		uneTable.setEnabled(false);

		// Arroundi et affecte les zones texte min, max et moy
		tempMin.setText(round.format(min));
		tempMax.setText(round.format(max));
		moy = moy / mesures.size();
		tempMoy.setText(round.format(moy));

		return uneTable;
	}

	// TODO factoriser le code avec setTable
	// TODO g�rer le choix du graphique
	/**
	 * <p>
	 * Impl&eacute;mente la biblioth&egrave;que JFreeChart :
	 * </p>
	 * <ol>
	 * <li>d&eacute;finit le type de container de donn&eacute;es -&gt;
	 * DefaultCategoryDataset</li>
	 * <li>alimente le container des donn&eacute;es</li>
	 * <li>Fabrique un graphique lin&eacute;aire -&gt;
	 * ChartFactory.createLineChart</li>
	 * <li>Englobe le graphique dans un panel sp&eacute;cifique -&gt; new
	 * ChartPanel(chart)</li>
	 * <li>Englobe ce panel dans un JPanle de l'IHM -&gt;
	 * pnlGraph.add(chartPanel)<br />
	 * </li>
	 * </ol>
	 * 
	 * @author J�r�me Valenti
	 * @see JFreeChart
	 */
	public void setChart() {

		Mesure mesure;
		
		int i1 = 0, i2 = 0, i3 = 0, i4 = 0;
		DefaultCategoryDataset dataChart = new DefaultCategoryDataset();

		// Set data ((Number)temp,zone,dateHeure)
		for (int i = 0; i < control.getMesures().size(); i++) {

			mesure = control.getMesures().get(i);

			switch (mesure.getNumZone()) {
			case "01":
				dataChart.addValue((Number) mesure.getCelsius(), mesure.getNumZone(), i1);
				i1++;
				break;
			case "02":
				dataChart.addValue((Number) mesure.getCelsius(), mesure.getNumZone(), i2);
				i2++;
				break;
			case "03":
				dataChart.addValue((Number) mesure.getCelsius(), mesure.getNumZone(), i3);
				i3++;
				break;
			case "04":
				dataChart.addValue((Number) mesure.getCelsius(), mesure.getNumZone(), i4);
				i4++;
				break;
			default:
				break;
			}
		}

		// un bouchon pour tester
		// Set data ((Number)temp,zone,dateHeure)
//        dataChart.addValue((Number)1.0, "01", 1);
//        dataChart.addValue((Number)5.0, "02", 1);
//        dataChart.addValue((Number)4.0, "01", 2);
//        dataChart.addValue((Number)7.0, "02", 2);
//        dataChart.addValue((Number)3.0, "01", 3);
//        dataChart.addValue((Number)6.0, "02", 3);
//        dataChart.addValue((Number)5.0, "01", 4);
//        dataChart.addValue((Number)8.0, "02", 4);
//        dataChart.addValue((Number)5.0, "01", 5);
//        dataChart.addValue((Number)4.0, "02", 5);
//        dataChart.addValue((Number)7.0, "01", 6);
//        dataChart.addValue((Number)4.0, "02", 6);
//        dataChart.addValue((Number)7.0, "01", 7);
//        dataChart.addValue((Number)2.0, "02", 7);
//        dataChart.addValue((Number)8.0, "01", 8);
//        dataChart.addValue((Number)1.0, "02", 8);
//		System.out.println(dataChart.getRowCount() + " lignes " + dataChart.getColumnCount() + " colonnes");

		JFreeChart chart = ChartFactory.createLineChart(null, // chart title
				"Heure", // domain axis label
				"Temp�ratures", // range axis label
				dataChart, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
		);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBounds(5, 20, 320, 190);
		chartPanel.setVisible(true);
		pnlGraph.add(chartPanel);
		System.out.println("chartPanel added to pnlGraph");
	}

	/**
	 * <p>
	 * Classe interne qui g�re le clique sur le bouton filtrer
	 * 
	 * @author J�r�me Valenti
	 */
	class filtrerData implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			control.setMesures(control.filtrerLesMesure(choixZone.getSelectedItem().toString()));
			System.out.println(
					"Filtrer Celsius : " + rdbtnCelsius.isSelected() + " Fahrenheit : " + rdbtnFahrenheit.isSelected()
							+ " choix : " + choixZone.getSelectedItem() + " d�but : " + startDate.getText());
			displayLesMesures(control.getMesures());

			// Construit le tableau d'objet
			laTable = setTable(control.getMesures());

			// Definit le JScrollPane qui va recevoir la JTable
			scrollPane.setViewportView(laTable);

			System.out.println("Before setChart in filtrerData()");
			// affiche le graphique
			setChart();
			System.out.println("After setChart in filtrerData()");
		}
	}

	private void displayLesMesures(ArrayList<Mesure> uneCollection) {

		for (int i = 0; i < uneCollection.size(); i++) {
			System.out.println(i + " " + uneCollection.get(i).getNumZone() + " | " + uneCollection.get(i).getHoroDate()
					+ " | " + uneCollection.get(i).getCelsius());
		}
	}
}