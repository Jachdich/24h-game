package com.cospox.idek;


public enum GeneType {
	NONE('N'),
	REPAIR_MEMBRANE('M'),
	DIVIDE('D'),
	PRODUCE_ATP('A'),
	COPY_GENE_INTO_RNA('R'),
	COPY_RNA_INTO_GENE('W'),
	INSERT_GENE_INTO_GENOME('I');
	private final char letter;
	
	GeneType(char string) {
		letter = string;
	}

	public String toString() {
		return String.valueOf(letter);
	}
	
	public int[] getColour() {
		switch (this) {
		case COPY_GENE_INTO_RNA: 	  return new int[]{0xFFFFFFFF, 0xFFFF22FF};
		case COPY_RNA_INTO_GENE:      return new int[]{0xFFFFFFFF, 0xFFFF9090};
		case DIVIDE:                  return new int[]{0xFFFFFFFF, 0xFFDD2211};
		case INSERT_GENE_INTO_GENOME: return new int[]{0xFFFFFFFF, 0xFF22FF44};
		case NONE:					  return new int[]{0xFFFFFFFF, 0xFF111111};
		case PRODUCE_ATP:			  return new int[]{0xFF000000, 0xFFFFFF00};
		case REPAIR_MEMBRANE:         return new int[]{0xFFFFFFFF, 0xFF2222FF};
		default:                      return new int[]{0xFFFFFFFF, 0xFF000000};
		}
	}

}
