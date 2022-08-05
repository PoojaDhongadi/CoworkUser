package microbi.organic.coworkuser.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import microbi.organic.coworkuser.R;

public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DataFish> data= Collections.emptyList();
    DataFish current;
    int currentPos=0;
    Selected selected;
    PreferenceManager preferenceManager;

    List<SeletedFeature> finalselect=new ArrayList<>();



    // create constructor to innitilize context and data sent from MainActivity
    public AdapterFish(Context context, List<DataFish> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.list_sub_feature_item, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }
    public ArrayList<SeletedFeature> getArrayList(){
        return (ArrayList<SeletedFeature>) finalselect;
    }


    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        preferenceManager = new PreferenceManager(context.getApplicationContext());
        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataFish current=data.get(position);
        myHolder.text_id.setText(current._id);
        myHolder.text_name.setText(current.name);
        myHolder.day_pass.setText(current.daypass);
        myHolder.month_pass.setText(current.monthlypass);

        selected = new Selected();


        myHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {

                   String daypassvalue = myHolder.day_pass.getText().toString();
                   String qantityvalue = myHolder.quantityed.getEditableText().toString();
                    String finalval = String.valueOf((Integer.parseInt(daypassvalue)*Integer.parseInt(qantityvalue)));
                  myHolder.totalcalu.setText(finalval);
                    if (preferenceManager.getKeyStatus("date") == "day") {
                        Toast.makeText(context, "show", Toast.LENGTH_SHORT).show();
                        SeletedFeature selections = new SeletedFeature();
                        selections.setId(myHolder.text_id.getText().toString());
                        selections.setRate(myHolder.day_pass.getText().toString());
                        selections.setQuantity(myHolder.quantityed.getEditableText().toString());
                        finalselect.add(selections);
                    } else {
                        SeletedFeature selections = new SeletedFeature();
                        selections.setId(myHolder.text_id.getText().toString());
                        selections.setRate(myHolder.day_pass.getText().toString());
                        selections.setQuantity(myHolder.quantityed.getEditableText().toString());
                        finalselect.add(selections);
                    }
                }

            }
        });

//        myHolder.textPrice.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
//
//

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        TextView text_id;
        TextView text_name;
        TextView day_pass;
        TextView month_pass;
        CheckBox checkBox;
        EditText quantityed;
        TextView totalcalu;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            text_id= (TextView) itemView.findViewById(R.id.subfe_id);
            text_name = (TextView) itemView.findViewById(R.id.sub_fea_name);
            day_pass = (TextView) itemView.findViewById(R.id.day_pass);
            month_pass = (TextView) itemView.findViewById(R.id.monthly_pass);
            checkBox = (CheckBox)itemView.findViewById(R.id.checkid);
            quantityed = (EditText)itemView.findViewById(R.id.quantity);
            totalcalu = (TextView) itemView.findViewById(R.id.totalcal);

        }

    }

}