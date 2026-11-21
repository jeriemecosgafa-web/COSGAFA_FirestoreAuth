package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView tvActionStatus;
    private boolean isFirewallEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Security check: if nobody is logged in, go back to LoginActivity
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        tvActionStatus = findViewById(R.id.tvActionStatus);
        tvActionStatus.setText("Active User: " + currentUser.getEmail());

        Button btnSaml = findViewById(R.id.btnPrimaryVibrantBlue);
        Button btnScan = findViewById(R.id.btnDeepPurple);
        Button btnFirewall = findViewById(R.id.btnBrightCyan);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Button 1 Functionality: Authenticate SAML Token
        btnSaml.setOnClickListener(v -> {
            tvActionStatus.setText("Status: SAML 2.0 Token Verified!");
            Toast.makeText(this, "SAML Token Authenticated for " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
        });

        // Button 2 Functionality: Run Vulnerability Scan
        btnScan.setOnClickListener(v -> {
            tvActionStatus.setText("Status: Vulnerability Scan Complete");
            new AlertDialog.Builder(this)
                    .setTitle("Cluster Security Scan")
                    .setMessage("Scan completed for " + currentUser.getEmail() + "\n\n• Critical Vulnerabilities: 0\n• TLS 1.3 Encryption: Active")
                    .setPositiveButton("Close", null)
                    .show();
        });

        // Button 3 Functionality: Toggle Zero-Trust Firewall
        btnFirewall.setOnClickListener(v -> {
            isFirewallEnabled = !isFirewallEnabled;
            if (isFirewallEnabled) {
                btnFirewall.setText("DisableZero-TrustFirewall");
                tvActionStatus.setText("Status: Zero-Trust Firewall ENABLED");
                Toast.makeText(this, "Zero-Trust Firewall Enabled", Toast.LENGTH_SHORT).show();
            } else {
                btnFirewall.setText("EnableZero-TrustFirewall");
                tvActionStatus.setText("Status: Zero-Trust Firewall DISABLED");
                Toast.makeText(this, "Zero-Trust Firewall Disabled", Toast.LENGTH_SHORT).show();
            }
        });

        // Button 4 Functionality: Logout
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }
}