package net.pechorina.kontempl.utils

import java.text.DecimalFormat

class UnitUtils {
	static String floatForm (double d) {
	   return new DecimalFormat("#.##").format(d);
	}

	static String bytesToHuman (long size) {
		long Kb = 1  * 1024
		long Mb = Kb * 1024
		long Gb = Mb * 1024
		long Tb = Gb * 1024
		long Pb = Tb * 1024
		long Eb = Pb * 1024

		if (size <  Kb)                 return floatForm(        size     ) + " byte";
		if (size >= Kb && size < Mb)    return floatForm((double)size / Kb) + " Kb";
		if (size >= Mb && size < Gb)    return floatForm((double)size / Mb) + " Mb";
		if (size >= Gb && size < Tb)    return floatForm((double)size / Gb) + " Gb";
		if (size >= Tb && size < Pb)    return floatForm((double)size / Tb) + " Tb";
		if (size >= Pb && size < Eb)    return floatForm((double)size / Pb) + " Pb";
		if (size >= Eb)                 return floatForm((double)size / Eb) + " Eb";

		return "???";
	}
}
