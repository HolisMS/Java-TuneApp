package satella.app.servicemoto.motor_page;

public class KmMotor {
    private String km_saat_ini;
    private String km_servis_terakhir;

    public KmMotor() {
    }

    public KmMotor(String km_saat_ini, String km_servis_terakhir) {
        this.km_saat_ini = km_saat_ini;
        this.km_servis_terakhir = km_servis_terakhir;
    }

    public String getKm_saat_ini() {
        return km_saat_ini;
    }

    public void setKm_saat_ini(String km_saat_ini) {
        this.km_saat_ini = km_saat_ini;
    }

    public String getKm_servis_terakhir() {
        return km_servis_terakhir;
    }

    public void setKm_servis_terakhir(String km_servis_terakhir) {
        this.km_servis_terakhir = km_servis_terakhir;
    }
}
