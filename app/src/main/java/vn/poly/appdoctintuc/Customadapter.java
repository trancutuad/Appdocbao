package vn.poly.appdoctintuc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Customadapter extends ArrayAdapter<DocBao> {

    public Customadapter(Context context, int resource, List<DocBao> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.row1, null);
        }
        DocBao p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri
            TextView txtdate = (TextView) view.findViewById(R.id.date);
            txtdate.setText(p.link);

            ImageView txtImage = (ImageView) view.findViewById(R.id.image_View);
            Glide.with(getContext()).load(p.image).into(txtImage);

        }
        return view;
    }

}
