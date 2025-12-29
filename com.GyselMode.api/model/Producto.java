package model;

public class Producto {
	 private int id;
	    private String nombre;
	    private String descripcion;
	    private double precio;
	    private String adicional;
	    private String talla;
	    private String color;
	    
	    private String imagen;

		public Producto(int id, String nombre, String descripcion, double precio, String adicional, String talla,
				String color, String imagen) {
			super();
			this.id = id;
			this.nombre = nombre;
			this.descripcion = descripcion;
			this.precio = precio;
			this.adicional = adicional;
			this.talla = talla;
			this.color = color;
			this.imagen = imagen;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}

		public double getPrecio() {
			return precio;
		}

		public void setPrecio(double precio) {
			this.precio = precio;
		}

		public String getAdicional() {
			return adicional;
		}

		public void setAdicional(String adicional) {
			this.adicional = adicional;
		}

		public String getTalla() {
			return talla;
		}

		public void setTalla(String talla) {
			this.talla = talla;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public String getImagen() {
			return imagen;
		}

		public void setImagen(String imagen) {
			this.imagen = imagen;
		}

		public Producto() {
			super();
		}
	    

}
