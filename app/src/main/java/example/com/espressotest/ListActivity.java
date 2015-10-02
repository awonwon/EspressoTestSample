package example.com.espressotest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GTW on 2015/10/1.
 */
public class ListActivity extends AppCompatActivity{
    List<Item> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setupListView();
    }

    private void setupListView(){
        items = new ArrayList<Item>();
        items.add(new Item("A1","Lorem ipsum dolor sit amet, consectetur adipiscing elit."));
        items.add(new Item("B2","Aliquam dignissim turpis cursus ipsum fringilla, eu iaculis turpis tincidunt."));
        items.add(new Item("C3","Donec eu libero porttitor, vulputate libero nec, tristique ipsum."));
        items.add(new Item("D4","Nam convallis felis sit amet nunc pulvinar, tincidunt sodales dui pharetra."));
        items.add(new Item("E5","Donec venenatis ex et dui elementum aliquam."));
        items.add(new Item("F6","Vivamus euismod nibh non massa faucibus vehicula."));
        items.add(new Item("G7","Curabitur id lacus blandit, cursus eros ac, placerat neque."));
        items.add(new Item("H8","Ut posuere arcu in malesuada volutpat."));
        items.add(new Item("I9","Quisque molestie nisl sit amet orci dignissim interdum."));
        items.add(new Item("J10","Vestibulum lacinia eros et varius ultrices."));
        items.add(new Item("K11","Maecenas elementum ante ac sem finibus molestie."));

        ListView listView = (ListView) findViewById(R.id.list);
        CustomAdapter adapter = new CustomAdapter(items, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item i = items.get(position);
                Toast.makeText(ListActivity.this, i.getTitle() + "\n" + i.getDescription(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class CustomAdapter extends BaseAdapter {
        List<Item> items = new ArrayList<Item>();
        Context c;

        public CustomAdapter(List<Item> items, Context context) {
            this.items = items;
            this.c = context;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Item i = items.get(position);

            if(convertView==null){
                convertView = LayoutInflater.from(c).inflate(R.layout.item, parent, false);
            }

            TextView title = ViewUtil.getHolder(convertView, R.id.title);
            title.setText(i.getTitle());

            TextView description = ViewUtil.getHolder(convertView, R.id.description);
            description.setText(i.getDescription());

            Switch insideSwitch = ViewUtil.getHolder(convertView, R.id.switch1);

            insideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    buttonView.setChecked(isChecked);
                }
            });

            return convertView;
        }
    }

    public class Item{
        private String title, description;

        public Item(String title, String description) {
            this.title = title;
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
