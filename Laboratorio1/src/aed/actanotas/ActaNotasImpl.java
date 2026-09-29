package aed.actanotas;

import java.util.Comparator;
import java.util.function.Function;

import es.upm.aedlib.Pair;
import es.upm.aedlib.indexedlist.ArrayIndexedList;
import es.upm.aedlib.indexedlist.IndexedList;
import es.upm.aedlib.map.HashTableMap;
import es.upm.aedlib.map.Map;

public class ActaNotasImpl implements ActaNotas{
	private String asignatura;
	private double notaMin;
	private int anyo;
	private boolean esExtraordinaria;
	private IndexedList<Calificacion> calificaciones;

	public ActaNotasImpl(String asignatura, double notaMinimaAprobado,
			int anyo, boolean esConvocatoriaExtraordinaria) {

		this.asignatura = asignatura;
		this.notaMin = notaMinimaAprobado;
		this.anyo = anyo;
		this.esExtraordinaria = esConvocatoriaExtraordinaria;
		this.calificaciones = new ArrayIndexedList<>();
	}

	@Override
	public String asignatura() {
		return asignatura;
	}

	@Override
	public int anyo() {
		return anyo;
	}

	@Override
	public boolean esConvocatoriaExtraordinaria() {
		return esExtraordinaria;
	}

	@Override
	public double minNotaAprobado() {
		return notaMin;
	}

	@Override
	public ActaNotas addCalificacion(String nombre, String matricula, String grupo, double nota) {
		if(nombre==null || matricula==null || grupo==null || nota<0.0 || nota>10.0) {
			throw new IllegalArgumentException();
		}	
		if(buscarMatricula(matricula)!=-1) {
			throw new IllegalStateException();
		}
		int idx=0;
		while(idx<calificaciones.size()&&matricula.compareTo(calificaciones.get(idx).matricula())>0) {
			idx++;
		}
		calificaciones.add(idx, new Calificacion(nombre, matricula, grupo, nota));

		return this;
	}

	@Override
	public Calificacion getCalificacion(String matricula) {
		if(matricula == null) {
			throw new IllegalArgumentException();
		}
		Calificacion calificacion = null;
		int pos = this.buscarMatricula(matricula);

		if(pos!=-1)
			calificacion=this.calificaciones.get(pos);


		return calificacion;
	}

	@Override
	public ActaNotas updateCalificacion(Calificacion calificacion) {
		if(calificacion==null) {
			throw new IllegalArgumentException();
		}
		int pos = buscarMatricula(calificacion.matricula());

		if (pos == -1) throw new IllegalStateException();

		calificaciones.set(pos, calificacion);

		return this;
	}

	@Override
	public ActaNotas deleteCalificacion(String matricula) {
		if(matricula == null) {
			throw new IllegalArgumentException();
		}
		int pos = buscarMatricula(matricula);
		if (pos == -1) throw new IllegalStateException();
		calificaciones.removeElementAt(pos);
		return this;
	}

	@Override
	public double notaMedia() {
		if(calificaciones.isEmpty()) {
			throw new IllegalStateException();
		}

		double sum = 0;

		for(Calificacion calificacion : calificaciones) {
			sum += calificacion.nota;
		}

		return sum/calificaciones.size();
	}

	@Override
	public IndexedList<Pair<String, Integer>> alumnosPorGrupo() {
		IndexedList<Pair<String,Integer>> lista = new ArrayIndexedList<>();
		Map<String, Integer> map = new HashTableMap<>();

		for(Calificacion calificacion : calificaciones) {
			if(map.containsKey(calificacion.grupo)) {
				map.put(calificacion.grupo, map.get(calificacion.grupo) + 1);
			} else {
				map.put(calificacion.grupo, 1);
			}
		}

		int i=0;
		for(String grupo : map.keys()) {
			lista.add(i++, new Pair<>(grupo,map.get(grupo)));
		}

		return lista;
	}

	@Override
	public IndexedList<Calificacion> getCalificaciones(Function<Calificacion, Boolean> filter,
			Comparator<Calificacion> cmp) {
		IndexedList<Calificacion> listCalificaciones = new ArrayIndexedList<>();

		if(filter==null) {
			filter = t -> true;
		}

		boolean yaOrdenado = (cmp == null);

		if(!yaOrdenado) {
			for(Calificacion calificacion : calificaciones) {
				if(filter.apply(calificacion)) {
					int idx=0;
					while(idx<listCalificaciones.size() &&
							cmp.compare(listCalificaciones.get(idx), calificacion) <= 0) {
						idx++;
					}

					listCalificaciones.add(idx, calificacion);
				}
			}
		}else {
			for(Calificacion calificacion : calificaciones) {
				if(filter.apply(calificacion)) {
					listCalificaciones.add(listCalificaciones.size(), calificacion);
				}
			}
		}

		return listCalificaciones;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ActaNotasImpl)) {
			return false;
		}

		ActaNotasImpl otroObj = (ActaNotasImpl) obj;
		return this.asignatura.equals(otroObj.asignatura) &&
				this.anyo == otroObj.anyo &&
				this.esExtraordinaria == otroObj.esExtraordinaria;
	}

	@Override
	public String toString() {
		return "Asignatura: "+asignatura+"\nNota Minima: "+notaMin+"\nAño: "+anyo+
				"\nEs extraordinaria"+esExtraordinaria+"\nCalificaciones:\n";
	}

	private int buscarMatricula(String matricula) {
		int low = 0;
		int high = calificaciones.size() - 1;

		while (low <= high) {
			int mid = (low + high) / 2;
			int cmp = calificaciones.get(mid).matricula().compareTo(matricula);

			if (cmp == 0) {
				return mid;
			} else if (cmp < 0) {
				low = mid + 1;
			} else {
				high = mid - 1;
			}
		}
		return -1;
	}



}
