package ec.edu.uce.pokedex.swing;

import ec.edu.uce.pokedex.entities.Pokemon;
import ec.edu.uce.pokedex.service.PokemonService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import java.net.URL;
import java.util.List;
import javax.swing.border.LineBorder;

public class PokedexGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField searchField;
    private JList<String> listPokemons;
    private DefaultListModel<String> pokemonListModel;
    private JLabel idPokemon, namePokemon, heightPokemon, weightPokemon, imagePokemon, timeLabel;
    private JList<String> abilitiesList, typesList;
    private PokemonService pokemonService;


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                PokemonService pokemonService = new PokemonService(); // Replace with your actual service initialization
                double timeData = 0.0;
                PokedexGUI frame = new PokedexGUI(pokemonService, timeData);
                frame.setVisible(true);
                frame.loadPokemons(); // Load Pokémon data on startup
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public PokedexGUI(PokemonService pokemonService, double timeData) {
        setTitle("POKEDEX");
        this.pokemonService = pokemonService;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 873, 556);
        contentPane = new JPanel();
        contentPane.setBackground(Color.RED);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBorder(null);
        panel.setBackground(Color.BLUE);
        panel.setBounds(43, 51, 302, 448);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblNewLabel = new JLabel("ORDENAR:");
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Cascadia Code PL", Font.BOLD, 13));
        lblNewLabel.setBounds(15, 414, 68, 23);
        panel.add(lblNewLabel);

        JComboBox<String> sortComboBox = new JComboBox<>(new String[]{"ASCENDENTE", "DESCENDENTE"});
        sortComboBox.setFont(new Font("Cascadia Code PL", Font.BOLD, 12));
        sortComboBox.setBounds(87, 414, 127, 23);
        panel.add(sortComboBox);

        JButton sortButton = new JButton("OK");
        sortButton.setFont(new Font("Cascadia Code PL", Font.BOLD, 13));
        sortButton.setBounds(224, 414, 59, 23);
        panel.add(sortButton);

        pokemonListModel = new DefaultListModel<>();
        listPokemons = new JList<>(pokemonListModel);
        listPokemons.setFont(new Font("Cascadia Code PL", Font.PLAIN, 13));
        listPokemons.setVisibleRowCount(20);
        listPokemons.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listPokemons);
        scrollPane.setBounds(15, 48, 270, 355);
        panel.add(scrollPane);

        JLabel lblTitle = new JLabel("POKEDEX");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Cascadia Code PL", Font.BOLD, 20));
        lblTitle.setBounds(20, 11, 242, 26);
        panel.add(lblTitle);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setBackground(new Color(217, 0, 0));
        detailsPanel.setBounds(391, 264, 445, 219);
        contentPane.add(detailsPanel);
        detailsPanel.setLayout(null);

        JLabel lblId = new JLabel("ID:");
        lblId.setFont(new Font("Cascadia Code PL", Font.BOLD, 13));
        lblId.setBounds(30, 49, 46, 14);
        detailsPanel.add(lblId);

        JLabel lblHeight = new JLabel("ALTURA:");
        lblHeight.setFont(new Font("Cascadia Code PL", Font.BOLD, 13));
        lblHeight.setBounds(30, 129, 73, 14);
        detailsPanel.add(lblHeight);

        JLabel lblWeight = new JLabel("PESO:");
        lblWeight.setFont(new Font("Cascadia Code PL", Font.BOLD, 13));
        lblWeight.setBounds(30, 169, 73, 14);
        detailsPanel.add(lblWeight);

        JLabel lblName = new JLabel("NOMBRE:");
        lblName.setFont(new Font("Cascadia Code PL", Font.BOLD, 13));
        lblName.setBounds(30, 89, 73, 14);
        detailsPanel.add(lblName);

        idPokemon = new JLabel("-");
        idPokemon.setFont(new Font("Cascadia Code PL", Font.PLAIN, 13));
        idPokemon.setBounds(113, 49, 148, 14);
        detailsPanel.add(idPokemon);

        namePokemon = new JLabel("-");
        namePokemon.setFont(new Font("Cascadia Code PL", Font.PLAIN, 13));
        namePokemon.setBounds(113, 89, 148, 14);
        detailsPanel.add(namePokemon);

        heightPokemon = new JLabel("-");
        heightPokemon.setFont(new Font("Cascadia Code PL", Font.PLAIN, 13));
        heightPokemon.setBounds(113, 129, 148, 14);
        detailsPanel.add(heightPokemon);

        weightPokemon = new JLabel("-");
        weightPokemon.setFont(new Font("Cascadia Code PL", Font.PLAIN, 13));
        weightPokemon.setBounds(113, 169, 148, 14);
        detailsPanel.add(weightPokemon);

        typesList = new JList<>(new DefaultListModel<>());
        typesList.setFont(new Font("Cascadia Code PL", Font.PLAIN, 13));
        typesList.setBounds(271, 140, 155, 62);
        detailsPanel.add(typesList);

        JLabel lblTypes = new JLabel("TIPOS:");
        lblTypes.setFont(new Font("Cascadia Code PL", Font.BOLD, 12));
        lblTypes.setBounds(271, 115, 73, 14);
        detailsPanel.add(lblTypes);

        JPanel panel_2 = new JPanel();
        panel_2.setBackground(new Color(255, 255, 255));
        panel_2.setBounds(10, 11, 425, 197);
        detailsPanel.add(panel_2);
        panel_2.setLayout(null);

        abilitiesList = new JList<>(new DefaultListModel<>());
        abilitiesList.setBounds(260, 33, 155, 62);
        panel_2.add(abilitiesList);
        abilitiesList.setFont(new Font("Cascadia Code PL", Font.PLAIN, 13));

        JLabel lblAbilities = new JLabel("HABILIDADES:");
        lblAbilities.setBounds(260, 8, 108, 14);
        panel_2.add(lblAbilities);
        lblAbilities.setFont(new Font("Cascadia Code PL", Font.BOLD, 12));

        JButton searchButton = new JButton("BUSCAR");
        searchButton.setFont(new Font("Cascadia Code PL", Font.BOLD, 13));
        searchButton.setBounds(609, 17, 89, 23);
        contentPane.add(searchButton);

        searchField = new JTextField();
        searchField.setFont(new Font("Cascadia Code PL", Font.PLAIN, 12));
        searchField.setBounds(364, 19, 224, 20);
        contentPane.add(searchField);
        searchField.setColumns(10);

        JComboBox<String> filterComboBox = new JComboBox<>(new String[]{"POKEMON", "TIPO", "HABILIDAD"});
        filterComboBox.setFont(new Font("Cascadia Mono PL", Font.BOLD, 13));
        filterComboBox.setBounds(166, 17, 179, 23);
        contentPane.add(filterComboBox);

        JLabel lblFilter = new JLabel("CONSULTAR POR:");
        lblFilter.setForeground(new Color(0, 0, 0));
        lblFilter.setFont(new Font("Cascadia Code PL", Font.BOLD, 13));
        lblFilter.setBounds(36, 19, 120, 19);
        contentPane.add(lblFilter);

        // Action Listeners
        searchButton.addActionListener(e -> searchPokemon(filterComboBox));

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(506, 61, 205, 180);
        contentPane.add(panel_1);

        imagePokemon = new JLabel();
        panel_1.add(imagePokemon);
        imagePokemon.setBackground(new Color(240,240,240));

        JLabel time = new JLabel("Tiempo subida de datos:");
        time.setForeground(new Color(0, 0, 0));
        time.setBackground(new Color(0, 0, 0));
        time.setFont(new Font("Cascadia Code PL", Font.PLAIN, 12));
        time.setBounds(557, 483, 167, 23);

        contentPane.add(time);

        JPanel panel_3 = new JPanel();
        panel_3.setBackground(new Color(217, 0, 0));
        panel_3.setBounds(496, 51, 224, 200);
        contentPane.add(panel_3);

        timeLabel = new JLabel("0.0 s");
        timeLabel.setFont(new Font("Cascadia Code PL", Font.BOLD, 12));
        timeLabel.setBounds(723, 483, 89, 22);
        timeLabel.setText(timeData + " s");
        contentPane.add(timeLabel);

        JLabel lblImage = new JLabel("");
        ImageIcon imagePokeball = new ImageIcon(ec.edu.uce.pokedex.swing.PokedexGUI.class.getResource("/images/pokeball.png"));
        Image imagePokeballScaled = imagePokeball.getImage().getScaledInstance(125, 125, Image.SCALE_SMOOTH);
        lblImage.setIcon(new ImageIcon(imagePokeballScaled));
        lblImage.setBounds(357, 93, 125, 125);
        contentPane.add(lblImage);
        sortButton.addActionListener(e -> sortPokemons(sortComboBox.getSelectedItem().toString()));
        listPokemons.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showPokemonDetails(listPokemons.getSelectedValue());
            }
        });
    }

    public void loadPokemons() {
        try {
            String query = searchField.getText();
            if(query.isEmpty()) {
                List<Pokemon> pokemons = pokemonService.getAllPokemonsSorted("asc");
                pokemonListModel.clear();
                pokemons.forEach(pokemon -> pokemonListModel.addElement(pokemon.getName()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading Pokémon data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchPokemon(JComboBox<String> filterComboBox) {
        try {
            String selectFilter = filterComboBox.getSelectedItem().toString();
            String query = searchField.getText();
            List<Pokemon> pokemons = pokemonService.getAllPokemonsSorted("asc");
            switch (selectFilter) {
                case "POKEMON":
                    // Lógica para buscar por nombre de Pokémon
                    pokemonListModel.clear();
                    pokemons.forEach(pokemon -> {
                        if (pokemon.getName().toLowerCase().startsWith(query.toLowerCase())) {
                            pokemonListModel.addElement(pokemon.getName());
                        }
                    });
                    searchField.setText("");
                    break;
                case "TIPO":
                    // Lógica para buscar por tipo de Pokémon
                    pokemons = pokemonService.getPokemonsByType(query);
                    pokemonListModel.clear();
                    pokemons.forEach(pokemon -> {
                        System.out.println(pokemon.getName());
                        pokemonListModel.addElement(pokemon.getName());
                    });
                    searchField.setText("");
                    break;
                case "HABILIDAD":
                    // Lógica para buscar por habilidad de Pokémon
                    pokemons = pokemonService.getPokemonsByAbility(query);
                    pokemonListModel.clear();
                    pokemons.forEach(pokemon -> {
                        System.out.println(pokemon.getName());
                        pokemonListModel.addElement(pokemon.getName());
                    });
                    searchField.setText("");
                    break;
                default:
                    System.out.println("Filtro desconocido");
                    break;
            }


        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar Pokémon: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sortPokemons(String order) {
        try {
            List<Pokemon> pokemons = pokemonService.getAllPokemonsSorted(order.toLowerCase());
            pokemonListModel.clear();
            pokemons.forEach(pokemon -> pokemonListModel.addElement(pokemon.getName()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al ordenar Pokémon: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showPokemonDetails(String pokemonName) {
        try {
            Pokemon pokemon = pokemonService.getPokemonByName(pokemonName);
            if (pokemon != null) {
                idPokemon.setText(String.valueOf(pokemon.getId()));
                namePokemon.setText(pokemon.getName());
                heightPokemon.setText(String.valueOf(pokemon.getHeight()));
                weightPokemon.setText(String.valueOf(pokemon.getWeight()));

                DefaultListModel<String> abilitiesModel = new DefaultListModel<>();
                pokemon.getAbilities().forEach(ability -> abilitiesModel.addElement(ability.getName()));
                abilitiesList.setModel(abilitiesModel);

                DefaultListModel<String> typesModel = new DefaultListModel<>();
                pokemon.getTypes().forEach(type -> typesModel.addElement(type.getName()));
                typesList.setModel(typesModel);

                ImageIcon pokemonImage = new ImageIcon(new URL(pokemon.getImage()));
                Image image = pokemonImage.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
                ImageIcon scaledImagePokemon = new ImageIcon(image);
                imagePokemon.setIcon(scaledImagePokemon);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al mostrar detalles: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
