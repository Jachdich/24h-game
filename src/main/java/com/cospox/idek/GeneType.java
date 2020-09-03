package com.cospox.idek;


public enum GeneType {
	NONE('N'),
	REPAIR_MEMBRANE('M'),
	DIVIDE('U'),
	PRODUCE_ATP('A'),
	GENE_TO_RNA('R'),
	RNA_TO_GENE('I'),
	DIVIDE_UNTIL('D'),
	MAKE_VIRUS('V'),
	REMOVE_WASTE('W');
	private final char letter;
	
	GeneType(char string) {
		letter = string;
	}

	public String toString() {
		return String.valueOf(letter);
	}
	
	public char getLetter() { return letter; }
	
	public int[] getColour() {
		switch (this) {
		case GENE_TO_RNA: 	          return new int[]{0xFFFFFFFF, 0xFFFF22FF};
		case DIVIDE:                  return new int[]{0xFFFFFFFF, 0xFFFF9090};
		case DIVIDE_UNTIL:            return new int[]{0xFFFFFFFF, 0xFFDD2211};
		case RNA_TO_GENE:             return new int[]{0xFFFFFFFF, 0xFF22FF44};
		case NONE:					  return new int[]{0xFFFFFFFF, 0xFF111111};
		case PRODUCE_ATP:			  return new int[]{0xFF000000, 0xFFFFFF00};
		case REPAIR_MEMBRANE:         return new int[]{0xFFFFFFFF, 0xFF2222FF};
		case REMOVE_WASTE:            return new int[]{0xFFFFFFFF, 0xFF804000};
		case MAKE_VIRUS:              return new int[]{0xFFFFFFFF, 0xFFFFA000};
		default:                      return new int[]{0xFFFFFFFF, 0xFF000000};
		}
	}

}
