package mx.mercatto.mercastock;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListaAdapter extends BaseAdapter {

    public class ViewHolder {
        TextView txtTitle, txtSubTitle;
    }

    List<Articulo> listaInicialSecundariaTemporal;
    public List<Categoria> listaInicial;
    public List<Articulo> listaSecundaria;
    public Activity context;
    ArrayList<Categoria> listaInicialTemporal;
    ArrayList<Articulo> listaSecundariaTemporal;
    public boolean bandera;
    public static int TRANSACCION=0;
    public static final int TRANSACCION_CATEGORIA=1;
    public static final int TRANSACCION_GENERAL=2;
    public ListaAdapter(List<Categoria> listaInicial, Activity context,int transaccion) {
        switch(transaccion){
            case TRANSACCION_CATEGORIA:
                this.listaInicial = listaInicial.subList(0,listaInicial.size()-1);
                this.context = context;
                listaInicialTemporal = new ArrayList<>();
                listaInicialSecundariaTemporal = new ArrayList<>();
                listaInicialTemporal.addAll(this.listaInicial);
                int i = 0;
                while (i < listaInicial.size()-1) {
                    listaInicialSecundariaTemporal.addAll(listaInicial.get(i).obtenerListaArticulo());
                    i++;
                }
                break;
            case TRANSACCION_GENERAL:
                this.listaInicial = listaInicial.subList(0,listaInicial.size()-1);
                this.context = context;
                listaInicialTemporal = new ArrayList<>();
                listaInicialSecundariaTemporal = new ArrayList<>();
                listaInicialTemporal.addAll(this.listaInicial);
                int j = listaInicial.size()-1;
                listaInicialSecundariaTemporal.addAll(listaInicial.get(j).obtenerListaArticulo());
                break;
        }
    }
    public ListaAdapter(List<Articulo> apps,Activity context,boolean bandera){
        this.listaSecundaria = apps;
        this.context = context;
        listaSecundariaTemporal = new ArrayList<>();
        listaSecundariaTemporal.addAll(listaSecundaria);
        this.bandera = bandera;
    }
    @Override
    public int getCount() {
        if(!bandera){
            return listaInicial.size();
        }else{
            return listaSecundaria.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private int[] colors = new int[] { 0x00000000, 0xAAF5F5F5 };

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder;
        int colorPos = position % colors.length;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_v, parent,false);
            viewHolder = new ViewHolder();
            viewHolder.txtTitle = (TextView) rowView.findViewById(R.id.title);
            viewHolder.txtSubTitle = (TextView) rowView.findViewById(R.id.subtitle);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(!bandera){
            viewHolder.txtTitle.setText(listaInicial.get(position).obtenerNombre());
             viewHolder.txtSubTitle.setText(listaInicial.get(position).obtenerAuxiliar());
        }else{
            viewHolder.txtTitle.setText(listaSecundaria.get(position).obtenerDescripcion());
            viewHolder.txtSubTitle.setText(listaSecundaria.get(position).obtenerClave());
        }
        rowView.setBackgroundColor(colors[colorPos]);
        return rowView;
    }
    public void filter(String charText) {
        try{
            charText = charText.toLowerCase(Locale.getDefault());
            listaInicial.clear();
            if (charText.length() == 0) {
                listaInicial.addAll(listaInicialTemporal);
            } else {
                for (Articulo postDetail : listaInicialSecundariaTemporal) {
                    if (charText.length() != 0 && postDetail.obtenerClave().toLowerCase(Locale.getDefault()).contains(charText)) {
                        ArrayList<Articulo> arr=new ArrayList<>();
                        arr.add(postDetail);
                        listaInicial.add(new Categoria(postDetail.obtenerCatId(),postDetail.obtenerClave(),postDetail.obtenerDescripcion(),"",arr));
                    }else if(charText.length() != 0 && postDetail.obtenerDescripcion().toLowerCase(Locale.getDefault()).contains(charText)) {
                        ArrayList<Articulo> arr=new ArrayList<>();
                        arr.add(postDetail);
                        listaInicial.add(new Categoria(postDetail.obtenerCatId(),postDetail.obtenerClave(),postDetail.obtenerDescripcion(),"",arr));
                    }else if(charText.length()!=0 &&postDetail.obtenerClaveAlterna().toLowerCase(Locale.getDefault()).contains(charText)){
                        ArrayList<Articulo> arr= new ArrayList<>();
                        arr.add(postDetail);
                        listaInicial.add(new Categoria(postDetail.obtenerCatId(),postDetail.obtenerClave(),postDetail.obtenerDescripcion(),"",arr));
                    }
                }
                if(!listaInicial.isEmpty()){
                    BGTCargarListadoGeneral.BANDERA_LISTA=BGTCargarListadoGeneral.BANDERA_BUSQUEDA;
                }
        }
           // listaCategoria = new ArrayList<>(new LinkedHashSet<>(listaCategoria));
        notifyDataSetChanged();
        }catch(Exception e){
            Log.d("",e.getMessage());
        }
    }

    public void filtroBusquedaTotal(String charText){
        try{
            charText = charText.toLowerCase(Locale.getDefault());
            listaInicial.clear();
            if (charText.length() == 0) {
                listaInicial.addAll(listaInicialTemporal);
            } else {
                for (Articulo postDetail : listaInicialSecundariaTemporal) {
                    if (charText.length() != 0 && postDetail.obtenerClave().toLowerCase(Locale.getDefault()).contains(charText)) {
                        ArrayList<Articulo> arr=new ArrayList<>();
                        arr.add(postDetail);
                        listaInicial.add(new Categoria(postDetail.obtenerCatId(),postDetail.obtenerClave(),postDetail.obtenerDescripcion(),"",arr));
                    }else if(charText.length() != 0 && postDetail.obtenerDescripcion().toLowerCase(Locale.getDefault()).contains(charText)) {
                        ArrayList<Articulo> arr=new ArrayList<>();
                        arr.add(postDetail);
                        listaInicial.add(new Categoria(postDetail.obtenerCatId(),postDetail.obtenerClave(),postDetail.obtenerDescripcion(),"",arr));
                    }else if(charText.length()!=0 &&postDetail.obtenerClaveAlterna().toLowerCase(Locale.getDefault()).contains(charText)){
                        ArrayList<Articulo> arr= new ArrayList<>();
                        arr.add(postDetail);
                        listaInicial.add(new Categoria(postDetail.obtenerCatId(),postDetail.obtenerClave(),postDetail.obtenerDescripcion(),"",arr));
                    }
                }
                if(!listaInicial.isEmpty()){
                    BGTCargarListadoGeneral.BANDERA_LISTA=BGTCargarListadoGeneral.BANDERA_BUSQUEDA;
                }
            }
            // listaCategoria = new ArrayList<>(new LinkedHashSet<>(listaCategoria));
            notifyDataSetChanged();
        }catch(Exception e){
            Log.d("",e.getMessage());
        }
    }
}


