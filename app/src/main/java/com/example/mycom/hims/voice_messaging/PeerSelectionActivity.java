package com.example.mycom.hims.voice_messaging;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mycom.hims.Common.MyAccount;
import com.example.mycom.hims.DialogActivity.ChanelAddDialogActivity;
import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.example.mycom.hims.R;
import com.example.mycom.hims.model.User;
import com.example.mycom.hims.model.api_response.GetUsersResponse;
import com.example.mycom.hims.scheduler.LoginActivity;
import com.example.mycom.hims.server_interface.SchedulerServerAPI;
import com.example.mycom.hims.thread.TimerDisplayThread;
import com.example.mycom.hims.thread.VMReceiverThread;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class PeerSelectionActivity extends Activity implements OnAsyncTaskCompleted{

    private Typeface font;
    private ImageView back;
    private TextView  selected_num;
    int selected_cnt = 0;
    TextView mBtn_ok;
    ArrayList<ItemsHolder> hold= new ArrayList<ItemsHolder>();
    ArrayList emp_names = new ArrayList();
    ArrayList if_checked = new ArrayList();
    ArrayList emp_id = new ArrayList();
   public static ArrayList selected_people = new ArrayList();
   public static ArrayList selected_id = new ArrayList();
    CustomAdapter cus;
    Bitmap images;

    private ListView mListView = null;

    private static Gson gson = new Gson();


    @Override
    public void onAsyncTaskCompleted(InputStream inputStream) {
        if (inputStream == null) {
            return;
        }

        Reader reader = new InputStreamReader(inputStream);
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        GetUsersResponse response = gson.fromJson(reader, GetUsersResponse.class);
        List<User> users = response.getUsers();

        for (User user : users) {
            if (user.getId().equals(LoginActivity.my_id)) {
                continue;
            }
            emp_id.add(user.getId());
            emp_names.add(user.getName());
            if_checked.add('0');
        }

        mListView = (ListView) findViewById(R.id.list);

        for(int q=0; q<emp_names.size(); q++){
//            if (!emp_names.get(q).toString().equals(ChannelListActivity.my_id)) {
            if (!emp_names.get(q).toString().equals(MyAccount.getInstance().getId())) {
                hold.add(new ItemsHolder(images, emp_names.get(q).toString()));
            }
        }
        cus = new CustomAdapter(hold);
        mListView.setAdapter(cus);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ItemsHolder ih = hold.get(position);
                if (Integer.parseInt(if_checked.get(position).toString()) == 0) {
                    ih.setImage(decodeImage(R.drawable.check_on));
                    cus.notifyDataSetChanged();
                    selected_people.add(ih.getName());
                    selected_id.add(emp_id.get(position));
                    selected_cnt++;
                    selected_num.setText(String.valueOf(selected_cnt));

                    if_checked.set(position, '1');

                    if (selected_cnt > 0) {
                        mBtn_ok.setEnabled(true);
                    }
                } else {
                    ih.setImage(decodeImage(R.drawable.check_none));
                    cus.notifyDataSetChanged();
                    selected_people.remove(ih.getName());
                    selected_cnt--;
                    selected_num.setText(String.valueOf(selected_cnt));

                    if_checked.set(position, '0');

                    if (selected_cnt == 0) {
                        back.setImageResource(R.drawable.back);
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_people);
        images = decodeImage(R.drawable.check_none);
        mBtn_ok = (TextView)findViewById(R.id.btn_ok);
        selected_people.clear();
        selected_id.clear();
        font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        selected_num = (TextView)findViewById(R.id.selected_num);
        back = (ImageView)findViewById(R.id.back);
        selected_num.setTypeface(font);

        SchedulerServerAPI.getUsersAsync(null, null, null, null, this);
        mBtn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeerSelectionActivity.this, ChanelAddDialogActivity.class);
                intent.putStringArrayListExtra("names", selected_people);
                startActivity(intent);
                finish();
            }
        });
            mBtn_ok.setEnabled(false);
    }


    public void go_back(View v){
        switch(v.getId()){
            case R.id.back:
                	Intent channelListIntent = new Intent(PeerSelectionActivity.this, ChannelListActivity.class);
                	startActivity(channelListIntent);
                    finish();

                break;
            default:
                break;

        }
    }

    @Override
	public void onBackPressed() {
    	Intent channelListIntent = new Intent(PeerSelectionActivity.this, ChannelListActivity.class);
    	startActivity(channelListIntent);
        finish();
	}

	private Bitmap decodeImage(int res) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), res);
        return bitmap;
    }


    /////////////////////////////////////리스트뷰///////////////////////////////////////////////
    class ItemsHolder
    {
        Bitmap image;
        String name;
        public ItemsHolder(Bitmap bitmap, String string) {
            // TODO Auto-generated constructor stub
            image = bitmap;
            name =string;
        }
        public Bitmap getImage() {
            return image;
        }
        public void setImage(Bitmap image) {
            this.image = image;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

    }
    class CustomAdapter extends BaseAdapter
    {

        LayoutInflater inflater;
        ArrayList<ItemsHolder> list;
        public CustomAdapter(ArrayList<ItemsHolder> list)
        {
            this.list=list;
            inflater= LayoutInflater.from(PeerSelectionActivity.this);

        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }
        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder;
            if(convertView==null)
            {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_item2, null);
                holder.iv= (ImageView) convertView.findViewById(R.id.if_selected);
                holder.tv = (TextView) convertView.findViewById(R.id.Emp_name);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();
            }
            ItemsHolder ih = list.get(position);
            holder.iv.setImageBitmap(ih.getImage());
            holder.tv.setText(ih.getName());
            return convertView;
        }

    }
    class ViewHolder
    {
        ImageView iv;
        TextView tv;
    }
}
