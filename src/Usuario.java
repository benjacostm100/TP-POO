public class Usuario {
    private String nombre;
    private String contraseña;
    private int puntaje;

    public Usuario(String nombre, String contraseña) {
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.puntaje = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContraseña() {
        return contraseña;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void sumarPuntos(int puntos) {
        this.puntaje += puntos;
    }
}
