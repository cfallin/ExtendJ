package bytecode;

import AST.Access;
import AST.ArrayTypeName;
import AST.Dims;
import AST.Dot;
import AST.IdDecl;
import AST.IdUse;
import AST.List;
import AST.Modifiers;
import AST.Opt;
import AST.ParameterDeclaration;
import AST.ParseName;


class TypeDescriptor {
	private Parser p;
	private String descriptor;
	public TypeDescriptor(Parser parser, String descriptor) {
		p = parser;
		this.descriptor = descriptor;
	}
	
	public boolean isBoolean() {
		return descriptor.charAt(0) == 'Z';
	}
	
	public Access type() {
		return type(descriptor);
	}
	
	public Access type(String s) {
		char c = s.charAt(0);
		switch (c) {
		case 'B':
			return new ParseName(new IdUse("byte"));
		case 'C':
			return new ParseName(new IdUse("char"));
		case 'D':
			return new ParseName(new IdUse("double"));
		case 'F':
			return new ParseName(new IdUse("float"));
		case 'I':
			return new ParseName(new IdUse("int"));
		case 'J':
			return new ParseName(new IdUse("long"));
		case 'S':
			return new ParseName(new IdUse("short"));
		case 'Z':
			return new ParseName(new IdUse("boolean"));
		case 'L':
			return this.p.fromClassName(s.substring(1, s.length() - 1));
		case '[':
			return new Dot(type(s.substring(1)), new ArrayTypeName(
					new List().add(new Dims(new Opt()))));
		case 'V':
			return new ParseName(new IdUse("void"));
		default:
			this.p.println("Error: unknown type in TypeDescriptor");
		}
		return null;
	}
	
	public List parameterList() {
		List list = new List();
		String s = descriptor;
		while(!s.equals("")) {
			s = typeList(s, list);
		}
		return list;
	}
	
	public String typeList(String s, List l) {
		char c = s.charAt(0);
		switch (c) {
		case 'B':
			l.add(new ParameterDeclaration(new Modifiers(), new ParseName(
					new IdUse("byte")), new IdDecl("p" + l.getNumChild())));
			return s.substring(1);
		case 'C':
			l.add(new ParameterDeclaration(new Modifiers(), new ParseName(
					new IdUse("char")), new IdDecl("p" + l.getNumChild())));
			return s.substring(1);
		case 'D':
			l.add(new ParameterDeclaration(new Modifiers(), new ParseName(
					new IdUse("double")), new IdDecl("p" + l.getNumChild())));
			return s.substring(1);
		case 'F':
			l.add(new ParameterDeclaration(new Modifiers(), new ParseName(
					new IdUse("float")), new IdDecl("p" + l.getNumChild())));
			return s.substring(1);
		case 'I':
			l.add(new ParameterDeclaration(new Modifiers(), new ParseName(
					new IdUse("int")), new IdDecl("p" + l.getNumChild())));
			return s.substring(1);
		case 'J':
			l.add(new ParameterDeclaration(new Modifiers(), new ParseName(
					new IdUse("long")), new IdDecl("p" + l.getNumChild())));
			return s.substring(1);
		case 'S':
			l.add(new ParameterDeclaration(new Modifiers(), new ParseName(
					new IdUse("short")), new IdDecl("p" + l.getNumChild())));
			return s.substring(1);
		case 'Z':
			l.add(new ParameterDeclaration(new Modifiers(), new ParseName(
					new IdUse("boolean")), new IdDecl("p" + l.getNumChild())));
			return s.substring(1);
		case 'L':
			//String[] strings = s.substring(1).split("\\;", 2);
			//l.add(new ParameterDeclaration(new Modifiers(),
			//		this.p.fromClassName(strings[0]),
			//		new IdDecl("p" + l.getNumChild())));
			//return strings[1];
      int pos = s.indexOf(';');
      String s1 = s.substring(1, pos);
      String s2 = s.substring(pos+1, s.length());
			l.add(new ParameterDeclaration(new Modifiers(),
					this.p.fromClassName(s1),
					new IdDecl("p" + l.getNumChild())));
			return s2;
		case '[':
			Dot d = new Dot();
			d.setRight(new ArrayTypeName(new List().add(new Dims(new Opt()))));
			l.add(new ParameterDeclaration(new Modifiers(), d, new IdDecl("p"
					+ l.getNumChild())));
			return arrayTypeList(s.substring(1), d);
		default:
			this.p.println("Error: unknown Type \"" + c + "\" in TypeDescriptor");
		}
		return "";

	}

	public String arrayTypeList(String s, Dot dot) {
		char c = s.charAt(0);
		switch (c) {
		case 'B':
			dot.setLeft(new ParseName(new IdUse("byte")));
			return s.substring(1);
		case 'C':
			dot.setLeft(new ParseName(new IdUse("char")));
			return s.substring(1);
		case 'D':
			dot.setLeft(new ParseName(new IdUse("double")));
			return s.substring(1);
		case 'F':
			dot.setLeft(new ParseName(new IdUse("float")));
			return s.substring(1);
		case 'I':
			dot.setLeft(new ParseName(new IdUse("int")));
			return s.substring(1);
		case 'J':
			dot.setLeft(new ParseName(new IdUse("long")));
			return s.substring(1);
		case 'S':
			dot.setLeft(new ParseName(new IdUse("short")));
			return s.substring(1);
		case 'Z':
			dot.setLeft(new ParseName(new IdUse("boolean")));
			return s.substring(1);
		case 'L':
			//String[] strings = s.substring(1).split("\\;", 2);
			//dot.setLeft(this.p.fromClassName(strings[0]));
			//return strings[1];
      int pos = s.indexOf(';');
      String s1 = s.substring(1, pos);
      String s2 = s.substring(pos+1, s.length());
			dot.setLeft(this.p.fromClassName(s1));
			return s2;
		case '[':
			Dot d = new Dot();
			dot.setLeft(d);
			d.setRight(new ArrayTypeName(new List().add(new Dims(new Opt()))));
			return arrayTypeList(s.substring(1), d);
		default:
			this.p.println("Error: unknown Type in TypeDescriptor");
		}
		return null;
	}

	
}