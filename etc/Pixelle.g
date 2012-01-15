/*
 * pixelle - Graphics algorithmic editor
 * Copyright (C) 2008 Dave Brosius
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
 grammar Pixelle;

@header {
package com.mebigfatguy.pixelle.antlr;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
}

@lexer::header {
package com.mebigfatguy.pixelle.antlr;
}

@members {
	ClassWriter cw;
	MethodVisitor mv;
	String clz;
	
	public PixelleParser(CommonTokenStream tokens, String clsName) {
		super(tokens, new RecognizerSharedState());
		
		clz = clsName.replace('.', '/');
		cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		cw.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC, clz, null, "java/lang/Object", new String[] {"com/mebigfatguy/pixelle/PixelleExpr"}); 

		cw.visitField(Opcodes.ACC_PRIVATE, "width", "I", null, Integer.valueOf(0));
		cw.visitField(Opcodes.ACC_PRIVATE, "height", "I", null, Integer.valueOf(0));
		
		mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, new String[0]);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0,0);
		
		mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "setOutputSize", "(II)V", null, new String[0]);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitInsn(Opcodes.DUP);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, clz, "width", "I");
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitFieldInsn(Opcodes.PUTFIELD, clz, "height", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0,0);
		
		mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "eval", "([Lcom/mebigfatguy/pixelle/PixelleEval;II)D", null, new String[0]);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitInsn(Opcodes.ARRAYLENGTH);
		mv.visitVarInsn(Opcodes.ISTORE, 4);
	}
	
	public byte[] getClassBytes() {
		return cw.toByteArray();
	}
}

@rulecatch {
   catch ( RecognitionException ex ) {
       throw ex;
   }
}

pixelle :	
	expr 
	{
		mv.visitInsn(Opcodes.DRETURN);
		mv.visitMaxs(0,0);
		cw.visitEnd();
	} ;
	
expr :
	cond_expr ;
	
cond_expr
	@init
	{
		Label falseLabel = null;
		Label continueLabel = null;
	}
	 :
	cond_and_expr 
	( '?' 
		{
			mv.visitInsn(Opcodes.DCONST_0); //FALSE
			mv.visitInsn(Opcodes.DCMPG);
			falseLabel = new Label();
			mv.visitJumpInsn(Opcodes.IFEQ, falseLabel);
		}
		expr ':' 
		{
			continueLabel = new Label();
			mv.visitJumpInsn(Opcodes.GOTO, continueLabel);
			mv.visitLabel(falseLabel);
		}
		expr 
		{
			mv.visitLabel(continueLabel);
		}
	)? ;
	
cond_and_expr 
	@init
	{
		Label false1Label = null;		
		Label false2Label = null;		
		Label continueLabel = null;
	}
	 :
	cond_or_expr 
	( '&&' cond_or_expr 
		{
			false1Label = new Label();
			false2Label = new Label();
			continueLabel = new Label();
			
			mv.visitInsn(Opcodes.DCONST_0);
			mv.visitInsn(Opcodes.DCMPG);
			mv.visitJumpInsn(Opcodes.IFEQ, false1Label);
			mv.visitInsn(Opcodes.DCONST_0);
			mv.visitInsn(Opcodes.DCMPG);
			mv.visitJumpInsn(Opcodes.IFEQ, false2Label);
			mv.visitInsn(Opcodes.DCONST_1);
			mv.visitJumpInsn(Opcodes.GOTO, continueLabel);
			mv.visitLabel(false1Label);
			mv.visitInsn(Opcodes.POP2);
			mv.visitLabel(false2Label);
			mv.visitInsn(Opcodes.DCONST_0);
			mv.visitLabel(continueLabel);
		}
	)* ;

cond_or_expr 
	@init
	{
		Label true1Label = null;		
		Label true2Label = null;		
		Label continueLabel = null;
	}
	 :
	eq_expr 
	( '||' eq_expr 
		{
			true1Label = new Label();
			true2Label = new Label();
			continueLabel = new Label();
			
			mv.visitInsn(Opcodes.DCONST_0);
			mv.visitInsn(Opcodes.DCMPG);
			mv.visitJumpInsn(Opcodes.IFNE, true1Label);
			mv.visitInsn(Opcodes.DCONST_0);
			mv.visitInsn(Opcodes.DCMPG);
			mv.visitJumpInsn(Opcodes.IFNE, true2Label);
			mv.visitInsn(Opcodes.DCONST_0);
			mv.visitJumpInsn(Opcodes.GOTO, continueLabel);
			mv.visitLabel(true1Label);
			mv.visitInsn(Opcodes.POP2);
			mv.visitLabel(true2Label);
			mv.visitInsn(Opcodes.DCONST_1);
			mv.visitLabel(continueLabel);	
		}
	)* ;
	
eq_expr 
@init
	{
		Label cmpSucceedLabel = null;
		Label continueLabel = null;
	}
	 :
	rel_expr 
	( op=('==' | '!=') rel_expr 
		{
			mv.visitInsn(Opcodes.DCMPG);
			cmpSucceedLabel = new Label();
			if ("==".equals($op.text))
			{
				mv.visitJumpInsn(Opcodes.IFEQ, cmpSucceedLabel);
			}
			else
			{
				mv.visitJumpInsn(Opcodes.IFNE, cmpSucceedLabel);
			}
			mv.visitInsn(Opcodes.DCONST_0); //FALSE
			continueLabel = new Label();
			mv.visitJumpInsn(Opcodes.GOTO, continueLabel);
			mv.visitLabel(cmpSucceedLabel);
			mv.visitInsn(Opcodes.DCONST_1); //TRUE
			mv.visitLabel(continueLabel);	
		} 
	)* ;
	
rel_expr 
@init
	{
		Label falseLabel = null;
		Label continueLabel = null;
	}
	 :   
	add_expr 
	( r=rel_op add_expr 
		{
			falseLabel = new Label();
			continueLabel = new Label();
			if ($r.text.equals("<=")) {
				mv.visitInsn(Opcodes.DCMPG);
				mv.visitJumpInsn(Opcodes.IFGT, falseLabel);
				mv.visitInsn(Opcodes.DCONST_1);
				mv.visitJumpInsn(Opcodes.GOTO, continueLabel);
				mv.visitLabel(falseLabel);
				mv.visitInsn(Opcodes.DCONST_0);
			} else if ($r.text.equals(">=")) {
				mv.visitInsn(Opcodes.DCMPG);
				mv.visitJumpInsn(Opcodes.IFLT, falseLabel);
				mv.visitInsn(Opcodes.DCONST_1);
				mv.visitJumpInsn(Opcodes.GOTO, continueLabel);
				mv.visitLabel(falseLabel);
				mv.visitInsn(Opcodes.DCONST_0);
			} else if ($r.text.equals("<")) {
				mv.visitInsn(Opcodes.DCMPG);
				mv.visitJumpInsn(Opcodes.IFGE, falseLabel);
				mv.visitInsn(Opcodes.DCONST_1);
				mv.visitJumpInsn(Opcodes.GOTO, continueLabel);
				mv.visitLabel(falseLabel);
				mv.visitInsn(Opcodes.DCONST_0);
			} else {
				mv.visitInsn(Opcodes.DCMPG);
				mv.visitJumpInsn(Opcodes.IFLE, falseLabel);
				mv.visitInsn(Opcodes.DCONST_1);
				mv.visitJumpInsn(Opcodes.GOTO, continueLabel);
				mv.visitLabel(falseLabel);
				mv.visitInsn(Opcodes.DCONST_0);
			}
			mv.visitLabel(continueLabel);
		}
	)* ;

rel_op :
    	('<=')
    |   ('>=')
    |   '<' 
    |   '>' 
    ;	
    
add_expr :
	mul_expr 
	(op=('+' | '-') mul_expr 
		{
			mv.visitInsn("+".equals($op.text)?Opcodes.DADD:Opcodes.DSUB);
		}
	)* ;
	
mul_expr	:
	unaryExpression 
	(op=('*' | '/') unaryExpression 
		{
			mv.visitInsn("*".equals($op.text)?Opcodes.DMUL:Opcodes.DDIV);
		}
	)* ;

unaryExpression : 
		'!' unaryExpression
		{
			Label falseLabel = new Label();
			Label continueLabel = new Label();
			mv.visitInsn(Opcodes.DCONST_0);
			mv.visitInsn(Opcodes.DCMPG);
			mv.visitJumpInsn(Opcodes.IFEQ, falseLabel);
			mv.visitInsn(Opcodes.DCONST_0);
			mv.visitJumpInsn(Opcodes.GOTO, continueLabel);
			mv.visitLabel(falseLabel);
			mv.visitInsn(Opcodes.DCONST_1);
			mv.visitLabel(continueLabel);
		}
	| '-' unaryExpression
		{
			mv.visitInsn(Opcodes.DNEG);
		}
	|	factor ;

factor	
	:	NUMBER 
		{
			mv.visitLdcInsn(Double.valueOf($NUMBER.text));
		}
	|	'(' expr ')'
	|   'p' 
		{
			mv.visitVarInsn(Opcodes.ALOAD, 1);
		}
		( '(' selector=expr ')' )?
		{
			if ($selector.text == null)
				mv.visitInsn(Opcodes.ICONST_0);
			else 
			{
				mv.visitInsn(Opcodes.D2I);
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "abs", "(I)I");
			}

			mv.visitVarInsn(Opcodes.ILOAD, 4);
			mv.visitInsn(Opcodes.IREM);
			mv.visitInsn(Opcodes.AALOAD);
		} 
		'[' expr 
		{ 
			mv.visitInsn(Opcodes.D2I); 
		} 
		',' expr 
		{ 
			mv.visitInsn(Opcodes.D2I); 
		} 
		']' '.' spec=('r'|'g'|'b'|'t'|'s') 
		{
			String s = $spec.text; 
			mv.visitLdcInsn(Character.valueOf(s.charAt(0))); 
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/mebigfatguy/pixelle/PixelleEval", "getValue", "(IIC)D" );
		} 
	| 	'x' 
		{
			mv.visitVarInsn(Opcodes.ILOAD, 2); 
			mv.visitInsn(Opcodes.I2D);
		}
	| 	'y' 
		{
			mv.visitVarInsn(Opcodes.ILOAD, 3); 
			mv.visitInsn(Opcodes.I2D);
		}
	|	'width' 
		{
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitFieldInsn(Opcodes.GETFIELD, clz, "width", "I");
			mv.visitInsn(Opcodes.I2D);
		}
	|	'height' 
		{
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitFieldInsn(Opcodes.GETFIELD, clz, "height", "I");
			mv.visitInsn(Opcodes.I2D);
		}
	|	'abs' '(' expr ')' 
		{
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "abs", "(D)D");
		}
	|	'max' '(' expr ',' expr ')' 
		{
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "max", "(DD)D");
		}
	|	'min' '(' expr ',' expr ')' 
		{
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "min", "(DD)D");
		} 
	|	'pow' '(' expr ',' expr ')' 
		{
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "pow", "(DD)D");
		} 
	|	'sqrt' '(' expr ')'
		{
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "sqrt", "(D)D");
		} 
	|	'sin' '(' expr ')'
		{
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "sin", "(D)D");
		} 
	|	'cos' '(' expr ')'
		{
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "cos", "(D)D");
		} 
	|	'tan' '(' expr ')'
		{
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "tan", "(D)D");
		} 
	|	'asin' '(' expr ')'
		{
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "asin", "(D)D");
		} 
	|	'acos' '(' expr ')'
		{
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "acos", "(D)D");
		} 
	|	'atan' '(' expr ')'
		{
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "atan", "(D)D");
		} 
	|	'log' '(' expr ')'
		{
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "log", "(D)D");
		} 
	|	'exp' '(' expr ')'
		{
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "exp", "(D)D");
		} 
	|	'e' '(' ')'
		{
			mv.visitLdcInsn(Double.valueOf(Math.E));
		}
	|	'pi' '(' ')'
		{
			mv.visitLdcInsn(Double.valueOf(Math.PI));
		}
	|	'random' '(' ')'
		{
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "random", "()D");
		} ;
	
NUMBER :   '0'..'9'+ ( '.' ('0'..'9'+))?;

WS :       (' '|'\t'|'\f'|'\n'|'\r')+{ skip(); };  



