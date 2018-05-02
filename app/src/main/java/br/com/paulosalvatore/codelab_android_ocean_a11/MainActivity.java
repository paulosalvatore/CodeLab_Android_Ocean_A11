package br.com.paulosalvatore.codelab_android_ocean_a11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void adicionarCompromisso(View view) {
		Intent intent = new Intent(this, AdicionarCompromissoActivity.class);
		startActivity(intent);
	}

	public void meusCompromissos(View view) {
		Intent intent = new Intent(this, MeusCompromissosActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menu) {
		Intent intent = null;

		switch (menu.getItemId()) {
			case R.id.mnAdicionarCompromisso:
				intent = new Intent(this, AdicionarCompromissoActivity.class);

				break;
			case R.id.mnMeusCompromissos:
				intent = new Intent(this, MeusCompromissosActivity.class);

				break;
		}

		if (intent != null) {
			startActivity(intent);
		}

		return super.onOptionsItemSelected(menu);
	}
}
