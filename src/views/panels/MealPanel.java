package views.panels;

import daoFactories.ContextFactory;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import models.*;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Vector;

public class MealPanel extends JPanel{
    private JButton addButton;
    private JButton deleteButton;
    private JTable table;
    private JCheckBox checkBoxes[];

    class Item
    {
        private long id;
        private String description;

        public Item(long id, String description)
        {
            this.id = id;
            this.description = description;
        }

        public long getId()
        {
            return id;
        }

        public String getDescription()
        {
            return description;
        }

        public String toString()
        {
            return description;
        }
    }


    public MealPanel(ObservableList<Food> foods, ObservableList<Unit> units, ObservableList<Group> groups, ObservableList<Location> locations, String[] mealTypes){

        // Initialize needed data to fill the tables and other fields
        Vector mealTypeVector = new Vector();
        for (String mealType:mealTypes){
            mealTypeVector.addElement(new Item(mealType.indexOf(mealType), mealType));
        }

        Vector foodVector = new Vector();
        for (Food food:foods){
            foodVector.addElement(new Item(food.getId(), food.getName() + " (" + food.getQuantity() + " " + units.stream().filter(u->u.getId() == food.getUnit_id()).findFirst().get().getName() + ")"));
        }

        Vector locationVector = new Vector();
        for (Location location:locations) {
            locationVector.addElement(new Item(location.getId(), location.getName()));
        }



        //Components


        JLabel mealTypeLabel = new JLabel("Meal Type:");
        JComboBox mealtypeCombobox = new JComboBox(mealTypeVector);

        JLabel foodNameLabel = new JLabel("Food Name:");
        JComboBox foodsCombox = new JComboBox(foodVector);


        JLabel locationLabel = new JLabel("Location:");
        JComboBox locationComboBox = new JComboBox(locationVector);

        JLabel amountLabel = new JLabel("Number of Servings:");
        JTextField amountTextField = new JTextField();

        JLabel dateLabel = new JLabel("Date:");
        JDatePicker datetimeTextField = new JDatePicker();

        JButton insertButton = new JButton("Add to Consumed Food List");
        insertButton.addActionListener(e -> {
            long foodCalorie = ContextFactory._FoodDao().findById(foodsCombox.getSelectedIndex()).getCalories();
//            long foodQuantity = ContextFactory._FoodDao().findById(foodsCombox.getSelectedIndex()).getQuantity();
//            long calPerQuantity = foodCalorie/foodQuantity;
            Meal meal = new Meal(foodsCombox.getSelectedIndex(),
                    mealtypeCombobox.getSelectedIndex(),
                    locationComboBox.getSelectedIndex(),
                    Long.parseLong(amountTextField.getText()),
                    foodCalorie*Long.parseLong(amountTextField.getText()),
                    LocalDateTime.now());
            ContextFactory._MealDao().insert(meal);
        });







        // Design
        setLayout(new BorderLayout(3,1));
        JPanel dataEntryPanel = new JPanel();
        dataEntryPanel.add(mealTypeLabel);
        dataEntryPanel.add(mealtypeCombobox);
        dataEntryPanel.setLayout(new GridLayout(0, 1));
        dataEntryPanel.add(foodNameLabel);
        dataEntryPanel.add(foodsCombox);
        dataEntryPanel.add(locationLabel);
        dataEntryPanel.add(locationComboBox);
        dataEntryPanel.add(amountLabel);
        dataEntryPanel.add(amountTextField);
        dataEntryPanel.add(dateLabel);
        dataEntryPanel.add(datetimeTextField);
        dataEntryPanel.add(insertButton);
        add(dataEntryPanel, BorderLayout.NORTH);

        // Listeners
        foods.addListener((ListChangeListener.Change<? extends Food> f) -> {
            JOptionPane.showMessageDialog(this, "Food Updated");
            foodVector.clear();
            for (Food food:foods){
                foodVector.addElement(new Item(food.getId(), food.getName()));
            }
            foodsCombox.updateUI();
        });

        locations.addListener((ListChangeListener.Change<? extends Location> l) -> {
            locationVector.clear();
            for (Location location:locations) {
                locationVector.addElement(new Item(location.getId(), location.getName()));
            }
            locationComboBox.updateUI();
        });


    }

}
