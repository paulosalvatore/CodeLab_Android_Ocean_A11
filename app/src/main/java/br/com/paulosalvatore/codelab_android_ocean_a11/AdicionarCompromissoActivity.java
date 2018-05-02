package br.com.paulosalvatore.codelab_android_ocean_a11;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static br.com.paulosalvatore.codelab_android_ocean_a11.NotificationPublisher.NOTIFICATION;
import static br.com.paulosalvatore.codelab_android_ocean_a11.NotificationPublisher.NOTIFICATION_ID;

public class AdicionarCompromissoActivity extends AppCompatActivity {

	private TextView tvData;
	private TextView tvHora;

	private EditText etTitulo;
	private EditText etLocalizacao;

	private ImageView ivMapa;

	private Button btInserirCompromisso;

	private Calendar calendar;

	private final String urlBase = "https://maps.google.com/maps/api/";
	private String key = "AIzaSyBXsvWOGTrufcD2fdNTPNldIPa-Fjwm2jo";

	private String titulo;
	private double latitude;
	private double longitude;
	private long data;

	private final String TAG = "LOCALIZAÇÃO";

	private long[] vibracao = new long[]{500, 100, 500};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adicionar_compromisso);

		tvData = findViewById(R.id.tvData);
		tvHora = findViewById(R.id.tvHora);

		etTitulo = findViewById(R.id.etTitulo);
		etLocalizacao = findViewById(R.id.etLocalizacao);

		ivMapa = findViewById(R.id.ivMapa);

		btInserirCompromisso = findViewById(R.id.btInserirCompromisso);

		calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 1);
		calendar.set(Calendar.MINUTE, 0);

		atualizarData();
		atualizarHora();

		final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
			                      int dayOfMonth) {
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, monthOfYear);
				calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

				atualizarData();

				tvData.setClickable(true);
			}
		};

		tvData.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tvData.setClickable(false);

				new DatePickerDialog(AdicionarCompromissoActivity.this, date, calendar
						.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				calendar.set(Calendar.MINUTE, minute);

				atualizarHora();

				tvHora.setClickable(true);
			}
		};

		tvHora.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tvHora.setClickable(false);

				new TimePickerDialog(AdicionarCompromissoActivity.this, time, calendar
						.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
			}
		});

		etLocalizacao.setOnEditorActionListener(
				new EditText.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH ||
								actionId == EditorInfo.IME_ACTION_DONE ||
								event != null &&
								event.getAction() == KeyEvent.ACTION_DOWN &&
								event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
							if (event == null || !event.isShiftPressed()) {
								pesquisarLocalizacao();

								return true;
							}
						}

						return false;
					}
				}
		);
	}

	public void pesquisarLocalizacao(View view) {
		pesquisarLocalizacao();
	}

	private void pesquisarLocalizacao() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(urlBase)
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		GoogleMapsService service = retrofit.create(GoogleMapsService.class);

		final ProgressDialog dialog = ProgressDialog.show(this, "Localização", "Buscando localização...");

		final String enderecoDigitado = etLocalizacao.getText().toString();

		service.buscarEndereco(enderecoDigitado)
				.enqueue(new Callback<ResultadoMaps>() {
					@Override
					public void onResponse(Call<ResultadoMaps> call, Response<ResultadoMaps> response) {
						try {
							List<Endereco> enderecos = response.body().getEnderecos();

							Endereco endereco = enderecos.get(0);
							EnderecoLocalizacao localizacao = endereco.getGeometria().getLocalizacao();

							latitude = localizacao.getLatitude();
							longitude = localizacao.getLongitude();

							atualizarImagem(enderecoDigitado, latitude, longitude, dialog);

							btInserirCompromisso.setVisibility(View.VISIBLE);
						}
						catch (Exception e) {
							Log.e(TAG, "Erro ao buscar endereço: " + e.toString());
							Toast.makeText(AdicionarCompromissoActivity.this, "Erro ao buscar endereço.", Toast.LENGTH_LONG).show();

							dialog.dismiss();
						}
					}

					@Override
					public void onFailure(Call<ResultadoMaps> call, Throwable t) {
						Log.e(TAG, "Erro ao buscar resultados:  " + t.toString());
						Toast.makeText(AdicionarCompromissoActivity.this, "Erro ao buscar resultados.", Toast.LENGTH_LONG).show();

						dialog.dismiss();
					}
				});

	}

	private void atualizarImagem(String endereco, double latitude, double longitude, final ProgressDialog dialog) {
		String markers = "color:red%7Clabel:L|" + latitude + "," + longitude;
		String urlImagem = urlBase + "staticmap?zoom=18&size=640x320&maptype=roadmap" +
				"&key=" + key + "&center=" + endereco + "&markers=" + markers;

		Picasso.with(AdicionarCompromissoActivity.this).load(urlImagem).into(ivMapa);

		dialog.dismiss();
	}

	private void atualizarData() {
		String formatoData = "EEE, d 'de' MMM 'de' yyyy";
		Locale local = new Locale("pt", "BR");
		SimpleDateFormat formatoDataSDF = new SimpleDateFormat(formatoData, local);
		tvData.setText(formatoDataSDF.format(calendar.getTime()));

		data = calendar.getTimeInMillis();
	}

	private void atualizarHora() {
		String formatoHora = "kk:mm";
		Locale local = new Locale("pt", "BR");
		SimpleDateFormat formatoHoraSDF = new SimpleDateFormat(formatoHora, local);
		tvHora.setText(formatoHoraSDF.format(calendar.getTime()));

		data = calendar.getTimeInMillis();
	}

	public void inserirCompromisso(View view) {
		DatabaseManager db = DatabaseManager.getInstance(this.getApplicationContext());

		titulo = etTitulo.getText().toString();

		Compromisso compromisso = new Compromisso(titulo, latitude, longitude, data);
		db.inserirCompromisso(compromisso);
		Toast.makeText(this, "Compromisso inserido com sucesso.", Toast.LENGTH_LONG).show();

		// agendarNotificacao("Compromisso", "Compromisso inserido com sucesso.", 0);

		criarNotificacao("Compromisso", "Compromisso inserido com sucesso.", true);

		this.finish();
	}

	private void agendarNotificacao(String titulo, String corpo, long delay) {
		Intent notificationIntent = new Intent(this, NotificationPublisher.class);
		notificationIntent.putExtra(NOTIFICATION_ID, 1);
		notificationIntent.putExtra(NOTIFICATION, criarNotificacao(titulo, corpo));
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		long tempoRealNotificacao = SystemClock.elapsedRealtime() + delay;
		AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, tempoRealNotificacao, pendingIntent);
	}

	private Notification criarNotificacao(String titulo, String corpo, boolean exibir) {
		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = criarNotificacao(titulo, corpo);

		if (exibir && notificationManager != null) {
			notificationManager.notify(1, notification);
		}

		return notification;
	}

	private Notification criarNotificacao(String titulo, String corpo) {
		Intent intent;
		PendingIntent pendingIntent;
		NotificationCompat.Builder builder;

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		String canalId = "CodeLabPushImagens 1";

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			String nome = getResources().getString(R.string.default_notification_channel_id);
			String descricao = "CodeLab Push Imagens - Channel";

			NotificationChannel channel = notificationManager.getNotificationChannel(canalId);

			if (channel == null) {
				int importancia = NotificationManager.IMPORTANCE_HIGH;
				channel = new NotificationChannel(canalId, nome, importancia);
				channel.setDescription(descricao);
				channel.enableVibration(true);
				channel.enableLights(true);
				channel.setLightColor(Color.RED);
				channel.setVibrationPattern(vibracao);
				notificationManager.createNotificationChannel(channel);
			}
		}

		builder = new NotificationCompat.Builder(this, canalId);

		intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

		builder.setContentTitle(titulo)
				.setSmallIcon(android.R.drawable.ic_popup_reminder)
				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setContentText(corpo)
				.setDefaults(Notification.DEFAULT_ALL)
				.setAutoCancel(true)
				.setContentIntent(pendingIntent)
				.setTicker(titulo)
				.setPriority(1)
				.setVibrate(vibracao);

		return builder.build();
	}
}
