package br.com.thiago.robotPi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.thiago.robotPi.R;
import br.com.thiago.robotPi.model.User;

public class UserAdapter extends BaseAdapter {
    private final List<User> users;
    private final Context context;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = users.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;
        /*
        if (view == null) {
            view = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView campoNome = (TextView) view.findViewById(R.id.item_nome);
        campoNome.setText(user.getNome());

        TextView campoTelefone = (TextView) view.findViewById(R.id.item_telefone);
        campoTelefone.setText(user.getTelefone());

        TextView campoUsername = (TextView) view.findViewById(R.id.item_username);
        if (campoUsername != null) {
            campoUsername.setText(user.getUsername());
        }

        TextView campoEmpresa = (TextView) view.findViewById(R.id.item_empresa);
        if (campoEmpresa != null) {
            campoEmpresa.setText(user.getEmpresa());
        }

        ImageView campoFoto = (ImageView) view.findViewById(br.com.thiago.robotPi.R.id.item_foto);
        String caminhoFoto = user.getCaminhoFoto();
        if (caminhoFoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            campoFoto.setImageBitmap(bitmapReduzido);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
        }*/

        return view;
    }
}
