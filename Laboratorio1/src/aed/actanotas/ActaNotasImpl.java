package aed.actanotas;

import java.util.Comparator;
import java.util.function.Function;

import es.upm.aedlib.Pair;
import es.upm.aedlib.indexedlist.ArrayIndexedList;
import es.upm.aedlib.indexedlist.IndexedList;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int anyo() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean esConvocatoriaExtraordinaria() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double minNotaAprobado() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ActaNotas addCalificacion(String nombre, String matricula, String grupo, double nota) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Calificacion getCalificacion(String matricula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActaNotas updateCalificacion(Calificacion calificacion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActaNotas deleteCalificacion(String matricula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double notaMedia() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IndexedList<Pair<String, Integer>> alumnosPorGrupo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndexedList<Calificacion> getCalificaciones(Function<Calificacion, Boolean> filter,
			Comparator<Calificacion> cmp) {
		// TODO Auto-generated method stub
		return null;
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
		return null;
	}
	
}
