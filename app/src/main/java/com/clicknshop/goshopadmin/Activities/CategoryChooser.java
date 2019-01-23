package com.clicknshop.goshopadmin.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.clicknshop.goshopadmin.Adapters.ExpandableListAdapter;
import com.clicknshop.goshopadmin.R;
import com.clicknshop.goshopadmin.Utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryChooser extends AppCompatActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_chooser);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        this.setTitle("Choose category");
//        category = findViewById(R.id.categoryChoosen);

        // preparing list data
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader,
                listDataChild,
                new ExpandableListAdapter.CategoryChoosen() {
                    @Override
                    public void whichCategory(String text, int position) {
//                        category.setText("Category: "+text);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("category", text);
                        returnIntent.putExtra("position", "" + position);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                });

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data


        // Adding child data
        List<String> oil = new ArrayList<String>();
        oil.add("Sunflower Oil");
        oil.add("Cooking Oil");
        oil.add("Other Edible oil");
        oil.add("Canola Oil");
        oil.add("Olive Oil");
        oil.add("Corn oil");
        oil.add("Ghee");
        oil.add("Desi Ghee");


        List<String> spices = new ArrayList<String>();
        spices.add("Herbs & Spices");
        spices.add("Salt");
        spices.add("Sugar");
        spices.add("National Masala");
        spices.add("Shan Masala");
        spices.add("Seasoning Cubes");
        spices.add("Vinegar");

        List<String> sauces = new ArrayList<String>();
        sauces.add("Ketchup");
        sauces.add("Chilli Sauce");
        sauces.add("Mayonnaise");
        sauces.add("American Garden");
        sauces.add("Nandos");
        sauces.add("Olives");
        sauces.add("Pickles");
        sauces.add("Other Sauces");

        List<String> daalain = new ArrayList<String>();
        daalain.add("Daalain");
        daalain.add("Rice");
        daalain.add("Flour");
        daalain.add("Dry Fruit");
        daalain.add("Other");


        List<String> jam = new ArrayList<String>();
        jam.add("Jam");
        jam.add("Honey");
        jam.add("Spread");
        jam.add("Syrup");

        List<String> bakingMix = new ArrayList<String>();
        bakingMix.add("Baking Mix");
        bakingMix.add("Jelly");
        bakingMix.add("Laziza Deserts");
        bakingMix.add("Other");

        List<String> polish = new ArrayList<String>();
        polish.add("Polish");
        polish.add("Brush");


        List<String> dairy = new ArrayList<String>();
        dairy.add("Dairy");
        dairy.add("Bakery");
        dairy.add("Beverages");
        dairy.add("Milk & Yogurt");
        dairy.add("Breads");
        dairy.add("Eggs");
        dairy.add("Mineral Water");

        List<String> menCare = new ArrayList<String>();
        menCare.add("M Roll on");
        menCare.add("Body Spray");
        menCare.add("Rozors");
        menCare.add("Shaving Foams");
        menCare.add("After Shave");

        List<String> womenCare = new ArrayList<String>();
        womenCare.add("W Body Spray");
        womenCare.add("W Roll on");
        womenCare.add("Pads");
        womenCare.add("Hair Remover");
        womenCare.add("Nail polish Remover");


        List<String> hairCare = new ArrayList<String>();
        hairCare.add("Hair Color");
        hairCare.add("Shampoo");
        hairCare.add("Conditioner");
        menCare.add("Gel");
        hairCare.add("Hair Cream");

        List<String> skinCare = new ArrayList<String>();
        skinCare.add("Scrubs");
        skinCare.add("Lotion & Cream");
        skinCare.add("Face Wash");
        skinCare.add("Sun Block");

        List<String> dentalCare = new ArrayList<String>();
        dentalCare.add("Tooth Brush");
        dentalCare.add("Tooth Paste");
        dentalCare.add("Mouth Wash");

        List<String> soap = new ArrayList<String>();
        soap.add("Soap");
        soap.add("Hand Wash");
        soap.add("Shower Gel");

        List<String> fruitsandveges = new ArrayList<String>();
        fruitsandveges.add("Fruits");
        fruitsandveges.add("Vegetables");


        List<String> homeCare = new ArrayList<String>();
        homeCare.add("Floor & Bath Cleaning");
        homeCare.add("Laundry");
        homeCare.add("Kitchen Cleaning");
        homeCare.add("Repellents");
        homeCare.add("Air Refreshners");
        homeCare.add("Cleaning Accessories");

        List<String> beverages = new ArrayList<String>();
        beverages.add("Cold Drinks");
        beverages.add("Juices");
        beverages.add("Tea");
        beverages.add("Mineral Water");
        beverages.add("Sharbat");
        beverages.add("Coffee");

        listDataHeader.add("Oil & Ghee");
        listDataHeader.add("Spices, Salt & Sugar");
        listDataHeader.add("Sauces, Olives & Pickles");
        listDataHeader.add("Daalain");
        listDataHeader.add("Jam and Honey");
        listDataHeader.add("Baking and Desert");
        listDataHeader.add("Polish and Brush");
        listDataHeader.add("Dairy");

        listDataHeader.add("Women Care");
        listDataHeader.add("Men Care");
        listDataHeader.add("Hair Care");
        listDataHeader.add("Skin Care");
        listDataHeader.add("Dental Care");
        listDataHeader.add("Soap, Hand Wash & Sanitizer");
        listDataHeader.add("Fruits & Vegetables");
        listDataHeader.add("Home Care");

        listDataChild.put(listDataHeader.get(0), oil); // Header, Child data
        listDataChild.put(listDataHeader.get(1), spices);
        listDataChild.put(listDataHeader.get(2), sauces);
        listDataChild.put(listDataHeader.get(3), daalain);
        listDataChild.put(listDataHeader.get(4), jam);
        listDataChild.put(listDataHeader.get(5), bakingMix);
        listDataChild.put(listDataHeader.get(6), polish);
        listDataChild.put(listDataHeader.get(7), dairy);
        listDataChild.put(listDataHeader.get(8), womenCare);
        listDataChild.put(listDataHeader.get(9), menCare);
        listDataChild.put(listDataHeader.get(10), hairCare);
        listDataChild.put(listDataHeader.get(11), skinCare);
        listDataChild.put(listDataHeader.get(12), dentalCare);
        listDataChild.put(listDataHeader.get(13), soap);
        listDataChild.put(listDataHeader.get(14), fruitsandveges);
        listDataChild.put(listDataHeader.get(15), homeCare);
//        listDataChild.put(listDataHeader.get(12), beverages);


    }

    @Override
    public void onBackPressed() {
        CommonUtils.showToast("Please choose category");
    }
}
