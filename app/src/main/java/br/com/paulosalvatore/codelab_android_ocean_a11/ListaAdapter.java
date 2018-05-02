package br.com.paulosalvatore.codelab_android_ocean_a11;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by paulo on 01/05/2018.
 */

class ListaAdapter extends ArrayAdapter<Compromisso> {
	private final Context context;
	private List<Compromisso> compromissos;

	public ListaAdapter(Context context, int resource, List<Compromisso> compromissos) {
		super(context, resource, compromissos);

		this.context = context;
		this.compromissos = compromissos;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.lista_layout, parent, false);
		}

		final Compromisso item = compromissos.get(position);

		if (item != null) {
			TextView campoId = row.findViewById(R.id.campoId);
			TextView campoLatitude = row.findViewById(R.id.campoLatitude);
			TextView campoLongitude = row.findViewById(R.id.campoLongitude);
			TextView campoDataHora = row.findViewById(R.id.campoDataHora);

			campoId.setText("ID: " + item.getId());
			campoLatitude.setText("Lat: " + item.getLatitude());
			campoLongitude.setText("Lon: " + item.getLongitude());

			long data = item.getData();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(data);
			String formatoDataHora = "EEE, d 'de' MMM 'de' yyyy, kk:mm";
			Locale local = new Locale("pt", "BR");
			SimpleDateFormat formatoHoraSDF = new SimpleDateFormat(formatoDataHora, local);
			campoDataHora.setText(formatoHoraSDF.format(calendar.getTime()));
		}

		return row;
	}
}
