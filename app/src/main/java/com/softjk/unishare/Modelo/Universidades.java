package com.softjk.unishare.Modelo;

public class Universidades {
    String Nombre, ABC,Clave,Tipo, Estado, Ubicacion, Telefono, Corre, Faceboock,Pagina, Logo, Campus, Municipio, Calificacion;
    public Universidades() {
    }

    public Universidades(String nombre, String ABC, String clave, String tipo, String estado, String ubicacion, String telefono, String corre, String faceboock, String pagina, String logo, String campus,String municipio, String calificacion) {
        Nombre = nombre;
        this.ABC = ABC;
        Clave = clave;
        Tipo = tipo;
        Estado = estado;
        Ubicacion = ubicacion;
        Telefono = telefono;
        Corre = corre;
        Faceboock = faceboock;
        Pagina = pagina;
        Logo = logo;
        Campus = campus;
        Municipio = municipio;
        Calificacion = calificacion;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        Ubicacion = ubicacion;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }

    public String getABC() {
        return ABC;
    }

    public void setABC(String ABC) {
        this.ABC = ABC;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getCorre() {
        return Corre;
    }

    public void setCorre(String corre) {
        Corre = corre;
    }

    public String getFaceboock() {
        return Faceboock;
    }

    public void setFaceboock(String faceboock) {
        Faceboock = faceboock;
    }

    public String getPagina() {
        return Pagina;
    }

    public void setPagina(String pagina) {
        Pagina = pagina;
    }

    public String getCampus() {
        return Campus;
    }

    public void setCampus(String campus) {
        Campus = campus;
    }

    public String getMunicipio() {
        return Municipio;
    }

    public void setMunicipio(String municipio) {
        Municipio = municipio;
    }

    public String getCalificacion() {
        return Calificacion;
    }

    public void setCalificacion(String calificacion) {
        Calificacion = calificacion;
    }
}
