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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import control.Config;
import control.Controller;
import model.Mesure;
import model.Stadium;
import view.component.ButtonRound;
import view.component.CheckBox;
import view.component.ComboBox;
import view.component.Label;
import view.component.Panel;
import view.component.RadioButton;
import view.component.ScrollPane;
import view.component.Slider;
import view.component.TextField;

/**
 * ConsoleGUI : IHM de l'application de consultation des temp�ratures Projet
 * Vinci Thermo Green
 * 
 * @author J�r�me Valenti
 * @version 2.0.0
 * @see control.Controller
 * @see model.Mesure
 */
public class ConsoleGUI extends JFrame {

	private static final long serialVersionUID = 5227009063967157518L;
	
	private Controller control;
	private Panel criteriaPanel = new Panel();

	private RadioButton rdbtnCelsius;
	private RadioButton rdbtnFahrenheit;

	private ComboBox<String> zoneDropdown = new ComboBox<String>();
	private TextField startDate;
	private TextField dateFin;

	private Panel pnlParam = new Panel();
	private Panel pnlGraph = new Panel();

	private TextField tempMin;
	private TextField tempMoy;
	private TextField tempMax;

	private Label selectedStadiumNameLabel;
	private Label alertLabel;

	private ComboBox<String> availableStadiumDropdown;

	private JTable laTable;

	private ScrollPane scrollPane = new ScrollPane();
	
	Slider overflowSliderMin;
	Slider overflowSliderMax;	

	private DefaultCategoryDataset dataChart = new DefaultCategoryDataset();
	private JFreeChart chart;
	private Stadium currentStadium;

	private boolean isReady;

	private HashMap<String, Stadium> loadedStadiums;

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
	Panel pnlBounds = new Panel();
	private TextField lblOverflowMin;
	private TextField lblOverflowMax;

	public ConsoleGUI(Controller controller) throws ParseException {

		this.control = controller;
		this.getContentPane().setBackground(Config.SECONDARY_COLOR);

		rdbtnCelsius = new RadioButton(control.getResourceBundle().getString("consoleGUIViewCelsius"));
		rdbtnCelsius.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				filter();
			}
		});
		rdbtnFahrenheit = new RadioButton(control.getResourceBundle().getString("consoleGUIViewFarhenheit"));
		rdbtnFahrenheit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filter();
			}
		});

		loadedStadiums = new HashMap<>();
		isReady = false;

		setIconImage(Toolkit.getDefaultToolkit().getImage("img/vinci_ico.jpg"));
		setTitle(control.getResourceBundle().getString("consoleGUIViewWindowTitle"));
		setSize(712, 560);
		setResizable(false);
		setFont(new Font("Consolas", Font.PLAIN, 12));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Pane pointe sur le container racine
		Container pane = getContentPane();
		// Fixe le Layout de la racine � Absolute
		getContentPane().setLayout(null);

		// D�finit le JPanel des crit�res
		criteriaPanel.setBounds(12, 56, 325, 145);
		criteriaPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Filtrage",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
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

		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnCelsius);
		group.add(rdbtnFahrenheit);
		zoneDropdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isReady)
					filter();
			}
		});

		zoneDropdown.setBounds(115, 50, 100, 20);
		criteriaPanel.add(zoneDropdown);

		Label zoneLabel = new Label(control.getResourceBundle().getString("consoleGUIViewZone"));
		zoneLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
		zoneLabel.setBounds(15, 54, 99, 14);
		criteriaPanel.add(zoneLabel);

		Label startLabel = new Label(control.getResourceBundle().getString("consoleGUIViewFrom"));
		startLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
		startLabel.setBounds(15, 83, 46, 14);
		criteriaPanel.add(startLabel);

		startDate = new TextField();
		startDate.setBounds(115, 79, 100, 20);
		criteriaPanel.add(startDate);
		startDate.setColumns(10);

		Label endLabel = new Label(control.getResourceBundle().getString("consoleGUIViewTo"));
		endLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
		endLabel.setBounds(15, 114, 46, 14);
		criteriaPanel.add(endLabel);

		dateFin = new TextField();
		dateFin.setColumns(10);
		dateFin.setBounds(115, 110, 100, 20);
		criteriaPanel.add(dateFin);

		Label vinciLogoLabel = new Label("");
		vinciLogoLabel.setIcon(new ImageIcon("img/s_vinci.png"));
		vinciLogoLabel.setBounds(221, 11, 95, 35);
		criteriaPanel.add(vinciLogoLabel);

		// D�finit le JScrollPane qui re�oit la JTable
		scrollPane.setBounds(12, 206, 325, 310);
		pane.add(scrollPane);

		// D�finit le JPanel des param�tres du graphique
		pnlParam.setBounds(342, 56, 355, 335);
		pnlParam.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
				control.getResourceBundle().getString("consoleGUIViewGraphTemp"), TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(128, 128, 128)));
		pnlParam.setLayout(null);
		pane.add(pnlParam);

		CheckBox chckbxDistinctZone = new CheckBox(
				control.getResourceBundle().getString("consoleGUIViewDistinguishZones"));
		chckbxDistinctZone.setEnabled(false);
		chckbxDistinctZone.setFont(new Font("Consolas", Font.PLAIN, 12));
		chckbxDistinctZone.setBounds(15, 20, 165, 23);
		pnlParam.add(chckbxDistinctZone);

		Label lblTypeDeGraphique = new Label(control.getResourceBundle().getString("consoleGUIViewGraphType"));
		lblTypeDeGraphique.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblTypeDeGraphique.setBounds(15, 50, 120, 14);
		pnlParam.add(lblTypeDeGraphique);

		ComboBox<String> choixGraphique = new ComboBox<>();
		choixGraphique.setEnabled(false);
		choixGraphique.setBounds(152, 47, 190, 20);
		pnlParam.add(choixGraphique);

		ButtonRound refreshButton = new ButtonRound(control.getResourceBundle().getString("consoleGUIViewRefresh"), 222,
				19);
		refreshButton.setBounds(222, 19, 120, 24);
		refreshButton.setEnabled(false);
		pnlParam.add(refreshButton);

		Label lblMin = new Label(control.getResourceBundle().getString("consoleGUIViewMin"));
		lblMin.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblMin.setBounds(15, 306, 30, 14);
		pnlParam.add(lblMin);

		tempMin = new TextField();
		tempMin.setEditable(false);
		tempMin.setBounds(55, 302, 50, 20);
		pnlParam.add(tempMin);
		tempMin.setColumns(10);

		Label lblMoy = new Label(control.getResourceBundle().getString("consoleGUIViewAvg"));
		lblMoy.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblMoy.setBounds(137, 304, 30, 14);
		pnlParam.add(lblMoy);

		tempMoy = new TextField();
		tempMoy.setEditable(false);
		tempMoy.setColumns(10);
		tempMoy.setBounds(177, 300, 50, 20);
		pnlParam.add(tempMoy);

		Label lblMax = new Label(control.getResourceBundle().getString("consoleGUIViewMax"));
		lblMax.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblMax.setBounds(252, 304, 30, 14);
		pnlParam.add(lblMax);

		tempMax = new TextField();
		tempMax.setEditable(false);
		tempMax.setColumns(10);
		tempMax.setBounds(292, 300, 50, 20);
		pnlParam.add(tempMax);

		// D�finit le JPanel qui recoit le graphique
		pnlGraph.setBorder(new TitledBorder(null, control.getResourceBundle().getString("consoleGUIViewGraph"),
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlGraph.setBounds(15, 75, 330, 215);

		// pose le pnlGraph dans le pnlParam
		pnlParam.add(pnlGraph);
		pnlGraph.setLayout(null);

		// D�finit le JPanel des bornes nominales
		pnlBounds.setBounds(342, 392, 355, 124);
		pnlBounds.setBorder(
				new TitledBorder(null, control.getResourceBundle().getString("consoleGUIViewOverflowNominalValues"),
						TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		pnlBounds.setLayout(null);
		pane.add(pnlBounds);

		overflowSliderMin = new Slider();
		overflowSliderMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent c) {
				controller.setOverflowMin(((Slider) c.getSource()).getValue());
				controller.updateOverflowStatus();
				lblOverflowMin.setText(String.valueOf(((Slider) (c.getSource())).getValue()));
			}
		});
		overflowSliderMin.setBounds(16, 40, 240, 25);
		pnlBounds.add(overflowSliderMin);

		overflowSliderMax = new Slider();
		overflowSliderMax.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent c) {
				controller.setOverflowMax(((Slider) c.getSource()).getValue());
				controller.updateOverflowStatus();
				lblOverflowMax.setText(String.valueOf(((Slider) (c.getSource())).getValue()));
			}
		});

		overflowSliderMax.setBounds(15, 88, 240, 25);
		pnlBounds.add(overflowSliderMax);

		Label lblDebordMin = new Label(control.getResourceBundle().getString("consoleGUIViewMinimum"));
		lblDebordMin.setBounds(15, 20, 79, 14);
		pnlBounds.add(lblDebordMin);

		Label lblDebordMaximum = new Label(control.getResourceBundle().getString("consoleGUIViewMaximum"));
		lblDebordMaximum.setBounds(15, 70, 79, 14);
		pnlBounds.add(lblDebordMaximum);

		alertLabel = new Label("");
		alertLabel.setIcon(new ImageIcon("img/s_green_button.png"));
		alertLabel.setBounds(270, 42, 75, 75);
		pnlBounds.add(alertLabel);

		lblOverflowMin = new TextField();
		lblOverflowMin.setBounds(91, 16, 114, 21);
		pnlBounds.add(lblOverflowMin);
		lblOverflowMin.setColumns(10);

		lblOverflowMax = new TextField();
		lblOverflowMax.setBounds(91, 67, 114, 21);
		pnlBounds.add(lblOverflowMax);
		lblOverflowMax.setColumns(10);

		Panel multiStadiumPanel = new Panel();
		multiStadiumPanel.setBounds(12, 12, 680, 39);
		getContentPane().add(multiStadiumPanel);
		multiStadiumPanel.setLayout(null);

		availableStadiumDropdown = new ComboBox<String>();
		availableStadiumDropdown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isReady) {
					controller
							.updateConsoleView(loadedStadiums.get((String) availableStadiumDropdown.getSelectedItem()));
				}
				selectedStadiumNameLabel
						.setText("Stade selectionné : " + (String) availableStadiumDropdown.getSelectedItem());
			}
		});
		availableStadiumDropdown.setBounds(0, 0, 122, 26);
		multiStadiumPanel.add(availableStadiumDropdown);

		selectedStadiumNameLabel = new Label("<placeholder>");
		selectedStadiumNameLabel.setBounds(443, 0, 237, 17);
		multiStadiumPanel.add(selectedStadiumNameLabel);

		this.setLocation(100, 100);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				controller.quit();
			}
		});
	}

	public void updateTable() {
		control.setMesures(Mesure.getMesuresFromStadiumID(currentStadium.getStadiumID()));
		laTable = setTable(Mesure.getMesuresFromStadiumID(currentStadium.getStadiumID()));
		scrollPane.remove(laTable);
		scrollPane.setViewportView(laTable);
		setChart();
	}

	/**
	 * Populate stadium dropdown Creates hashmap with each stadium IDs with the
	 * corresponding stadium.
	 * 
	 * @param stadiums
	 */
	public void populateStadiumDropdown(ArrayList<Stadium> stadiums) {
		loadedStadiums.clear();
		this.isReady = false;

		for (Stadium stadium : stadiums) {
			availableStadiumDropdown.addItem(stadium.getStadiumName());
			loadedStadiums.put(stadium.getStadiumName(), stadium);
		}

		this.isReady = true;
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

		// laTable.setModel(new DefaultTableModel());

		if (mesures.isEmpty())
			return this.laTable;

		if (rdbtnCelsius.isSelected()) {

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

		String[] titreColonnes = { control.getResourceBundle().getString("consoleGUIViewZone"),
				control.getResourceBundle().getString("consoleGUIViewDateHour"),
				control.getResourceBundle().getString("consoleGUIViewTemp") };
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
		ArrayList<Mesure> mesures = Mesure.getMesuresFromStadiumID(currentStadium.getStadiumID());

		int i1 = 0, i2 = 0, i3 = 0, i4 = 0;

		// Set data ((Number)temp,zone,dateHeure)
		for (int i = 0; i < mesures.size(); i++) {

			mesure = mesures.get(i);

			switch (mesure.getNumZone()) {
			case 1:
				dataChart.addValue((Number) mesure.getCelsius(), mesure.getNumZone(), i1);
				i1++;
				break;
			case 2:
				dataChart.addValue((Number) mesure.getCelsius(), mesure.getNumZone(), i2);
				i2++;
				break;
			case 3:
				dataChart.addValue((Number) mesure.getCelsius(), mesure.getNumZone(), i3);
				i3++;
				break;
			case 4:
				dataChart.addValue((Number) mesure.getCelsius(), mesure.getNumZone(), i4);
				i4++;
				break;
			default:
				break;
			}
		}

		chart = ChartFactory.createLineChart(null, // chart title
				control.getResourceBundle().getString("consoleGUIViewHours"), // domain axis label
				control.getResourceBundle().getString("consoleGUIViewTemperature"), // range axis label
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
	}

	public void updateGraph() {
		Mesure mesure;
		ArrayList<Mesure> mesures = Mesure.getMesuresFromStadiumID(currentStadium.getStadiumID());
		dataChart.clear();
		int i1 = 0, i2 = 0, i3 = 0, i4 = 0;

		// Set data ((Number)temp,zone,dateHeure)
		for (int i = 0; i < mesures.size(); i++) {
			mesure = mesures.get(i);

			switch (mesure.getNumZone()) {
			case 1:
				dataChart.addValue((Number) mesure.getCelsius(), mesure.getNumZone(), i1);
				i1++;
				break;
			case 2:
				dataChart.addValue((Number) mesure.getCelsius(), mesure.getNumZone(), i2);
				i2++;
				break;
			case 3:
				dataChart.addValue((Number) mesure.getCelsius(), mesure.getNumZone(), i3);
				i3++;
				break;
			case 4:
				dataChart.addValue((Number) mesure.getCelsius(), mesure.getNumZone(), i4);
				i4++;
				break;
			default:
				break;
			}
		}

		chart.getCategoryPlot().setDataset(dataChart);
		chart.fireChartChanged();
	}

	private void filter() {
		control.setMesures(Mesure.getMesuresFromStadiumID(currentStadium.getStadiumID()));

		control.setMesures(control.filtrerLesMesure(zoneDropdown.getSelectedItem().toString()));
		updateGraph();

		// Construit le tableau d'objet
		laTable = setTable(control.getMesures());

		// Definit le JScrollPane qui va recevoir la JTable
		scrollPane.setViewportView(laTable);

		setChart();
	}

	public void setAlertIcon(ImageIcon img) {
		alertLabel.setIcon(img);
	}

	public void setCurrentStadium(Stadium currentStadium) {
		this.currentStadium = currentStadium;
		ArrayList<String> zones = currentStadium.getZones();
		isReady = false;
		zoneDropdown.removeAllItems();
		zoneDropdown.addItem("*");

		for (String zone : zones) {
			zoneDropdown.addItem(zone);
		}

		isReady = true;
	}

	public Stadium getCurrentStadium() {
		return currentStadium;
	}
}