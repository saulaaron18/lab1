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
		if(nombre==null || matricula==null || grupo==null) {
			throw new IllegalArgumentException();
		}	
		if(getCalificacion(matricula)!=null) {
			throw new IllegalStateException();
		}

		calificaciones.add(calificaciones.size(), new Calificacion(nombre, matricula, grupo, nota));
		return this;
	}

	@Override
	public Calificacion getCalificacion(String matricula) {
		if(matricula == null) {
			throw new IllegalArgumentException();
		}
		Calificacion calificacion = null;

		for(int idx=0;idx<calificaciones.size();idx++) {
			if(calificaciones.get(idx).matricula.equals(matricula)) {
				calificacion = calificaciones.get(idx);
				break;
			}
		}

		return calificacion;
	}

	@Override
	public ActaNotas updateCalificacion(Calificacion calificacion) {
		if(calificacion==null) {
			throw new IllegalArgumentException();
		}
		Calificacion calificacionAntigua = getCalificacion(calificacion.matricula);

		if(calificacionAntigua == null) {
			throw new IllegalStateException();
		}

		calificaciones.set(calificaciones.indexOf(calificacionAntigua), calificacion);

		return this;
	}

	@Override
	public ActaNotas deleteCalificacion(String matricula) {
		if(matricula == null) {
			throw new IllegalArgumentException();
		}
		Calificacion calificacion = getCalificacion(matricula);

		if(calificacion == null) {
			throw new IllegalStateException();
		}

		calificaciones.remove(calificacion);
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
		 if(cmp==null) {
			 cmp = (o1, o2) -> o1.matricula.compareTo(o2.matricula);
		 }
		 
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
		String result = "";
		
		for(Calificacion calificacion : calificaciones) {
			result += calificacion+"\n";
		}
		
		return "Asignatura: "+asignatura+"\nNota Minima: "+notaMin+"\nAño: "+anyo+
				"\nEs extraordinaria"+esExtraordinaria+"\nCalificaciones:\n"+result;
	}

}
