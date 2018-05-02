package br.com.paulosalvatore.codelab_android_ocean_a11;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class MeusCompromissosActivity extends AppCompatActivity {

	private ListView lvLista;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meus_compromissos);

		lvLista = findViewById(R.id.lvLista);

		DatabaseManager db = DatabaseManager.getInstance(this.getApplicationContext());
		List<Compromisso> compromissos = db.obterCompromissos();

		lvLista.setAdapter(
				new ListaAdapter(
						this,
						R.layout.lista_layout,
						compromissos
				)
		);
	}
}
