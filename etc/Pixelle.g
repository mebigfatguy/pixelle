/*
 * pixelle - Graphics algorithmic editor
 * Copyright (C) 2008-2012 Dave Brosius
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
		cw.visitField(Opcodes.ACC_PRIVATE, "stack", "Ljava/util/List;", "Ljava/lang/List<Ljava/lang/Double;>;", null);
		
		mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, new String[0]);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitTypeInsn(Opcodes.NEW, "java/util/ArrayList");
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V");
        mv.visitFieldInsn(Opcodes.PUTFIELD, clz, "stack", "Ljava/util/List;"); 
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
		
		mv = cw.visitMethod(Opcodes.ACC_PRIVATE, "pixelAverage", "(DDDCLcom/mebigfatguy/pixelle/PixelleEval;)D", null, new String[0]);
        
		mv.visitVarInsn(Opcodes.DLOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "round", "(D)J");
		mv.visitInsn(Opcodes.L2I);
		mv.visitVarInsn(Opcodes.ISTORE, 9);
        mv.visitVarInsn(Opcodes.DLOAD, 3);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "round", "(D)J");
        mv.visitInsn(Opcodes.L2I);
        mv.visitVarInsn(Opcodes.ISTORE, 10);
        mv.visitVarInsn(Opcodes.DLOAD, 5);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "round", "(D)J");
        mv.visitInsn(Opcodes.L2I);
        mv.visitVarInsn(Opcodes.ISTORE, 11);		
		mv.visitInsn(Opcodes.DCONST_0);
		mv.visitVarInsn(Opcodes.DSTORE, 12);
		mv.visitVarInsn(Opcodes.ILOAD, 10);
		mv.visitVarInsn(Opcodes.ILOAD, 11);
		mv.visitInsn(Opcodes.IADD);
        mv.visitVarInsn(Opcodes.ISTORE, 14);
		mv.visitVarInsn(Opcodes.ILOAD, 9);
        mv.visitVarInsn(Opcodes.ILOAD, 11);
        mv.visitInsn(Opcodes.IADD);
        mv.visitVarInsn(Opcodes.ISTORE, 15);
        mv.visitVarInsn(Opcodes.ILOAD, 10);
        mv.visitVarInsn(Opcodes.ILOAD, 11);
        mv.visitInsn(Opcodes.ISUB);
        mv.visitVarInsn(Opcodes.ISTORE, 16);
        Label yBranchBottom = new Label();
        mv.visitJumpInsn(Opcodes.GOTO, yBranchBottom);
        Label yBranchTop = new Label();
        mv.visitLabel(yBranchTop);
        mv.visitVarInsn(Opcodes.ILOAD, 9);
        mv.visitVarInsn(Opcodes.ILOAD, 11);        
        mv.visitInsn(Opcodes.ISUB);
        mv.visitVarInsn(Opcodes.ISTORE, 17);        
        Label xBranchBottom = new Label();
        mv.visitJumpInsn(Opcodes.GOTO, xBranchBottom);
        Label xBranchTop = new Label();
        mv.visitLabel(xBranchTop);
        mv.visitVarInsn(Opcodes.DLOAD, 12);
        mv.visitVarInsn(Opcodes.ALOAD, 8);
        mv.visitVarInsn(Opcodes.ILOAD, 17);
        mv.visitVarInsn(Opcodes.ILOAD, 16);         
        mv.visitVarInsn(Opcodes.ILOAD, 7);  
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/mebigfatguy/pixelle/PixelleEval", "getValue", "(IIC)D");
        mv.visitInsn(Opcodes.DADD);
        mv.visitVarInsn(Opcodes.DSTORE, 12);
        mv.visitIincInsn(17, 1);
        mv.visitLabel(xBranchBottom);
        mv.visitVarInsn(Opcodes.ILOAD, 17);
        mv.visitVarInsn(Opcodes.ILOAD, 15);
        mv.visitJumpInsn(Opcodes.IF_ICMPLE, xBranchTop); 
        mv.visitIincInsn(16, 1);
        mv.visitLabel(yBranchBottom);
        mv.visitVarInsn(Opcodes.ILOAD, 16);
        mv.visitVarInsn(Opcodes.ILOAD, 14);
        mv.visitJumpInsn(Opcodes.IF_ICMPLE, yBranchTop);
        mv.visitVarInsn(Opcodes.DLOAD, 12);
        mv.visitVarInsn(Opcodes.ILOAD, 11);
        mv.visitInsn(Opcodes.ICONST_2);
        mv.visitInsn(Opcodes.IMUL);
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitInsn(Opcodes.IADD);
        mv.visitInsn(Opcodes.DUP);
        mv.visitInsn(Opcodes.IMUL);
        mv.visitInsn(Opcodes.I2D);
        mv.visitInsn(Opcodes.DDIV);
        mv.visitInsn(Opcodes.DRETURN);
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

unaryExpression 
    :   '!' unaryExpression
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
	|   functionExpr
	|   specialExpr
    |   pixelExpr
    |   locationExpr;
		       
pixelExpr 
    :   P 
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
        ']' '.' spec=(R|G|B|T|S) 
        {
            String s = $spec.text; 
            mv.visitLdcInsn(Character.valueOf(s.charAt(0))); 
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/mebigfatguy/pixelle/PixelleEval", "getValue", "(IIC)D" );
        };
        
locationExpr
    :   X
        {
            mv.visitVarInsn(Opcodes.ILOAD, 2); 
            mv.visitInsn(Opcodes.I2D);
        }
    |   Y 
        {
            mv.visitVarInsn(Opcodes.ILOAD, 3); 
            mv.visitInsn(Opcodes.I2D);
        }
    |   WIDTH
        {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, clz, "width", "I");
            mv.visitInsn(Opcodes.I2D);
        }
    |   HEIGHT 
        {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, clz, "height", "I");
            mv.visitInsn(Opcodes.I2D);
        }; 
        
functionExpr
    :   ABS '(' expr ')' 
        {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "abs", "(D)D");
        }
    |   MAX '(' expr ',' expr ')' 
        {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "max", "(DD)D");
        }
    |   MIN '(' expr ',' expr ')' 
        {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "min", "(DD)D");
        } 
    |   POW '(' expr ',' expr ')' 
        {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "pow", "(DD)D");
        } 
    |   SQRT '(' expr ')'
        {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "sqrt", "(D)D");
        } 
    |   SIN '(' expr ')'
        {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "sin", "(D)D");
        } 
    |   COS '(' expr ')'
        {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "cos", "(D)D");
        } 
    |   TAN '(' expr ')'
        {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "tan", "(D)D");
        } 
    |   ASIN '(' expr ')'
        {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "asin", "(D)D");
        } 
    |   ACOS '(' expr ')'
        {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "acos", "(D)D");
        } 
    |   ATAN '(' expr ')'
        {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "atan", "(D)D");
        } 
    |   LOG '(' expr ')'
        {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "log", "(D)D");
        } 
    |   EXP '(' expr ')'
        {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "exp", "(D)D");
        } 
    |   E '(' ')'
        {
            mv.visitLdcInsn(Double.valueOf(Math.E));
        }
    |   PI '(' ')'
        {
            mv.visitLdcInsn(Double.valueOf(Math.PI));
        }
    |   RANDOM '(' ')'
        {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "random", "()D");
        } ;
        
specialExpr
    @init
    {
        Label leftLabel = null;
        Label rightLabel = null;        
        Label topLabel = null;
        Label bottomLabel = null;
        Label failureLabel = null;
        Label successLabel = null;
        Label exitLabel = null;
    }
    :   PIXELINRECT '(' expr ',' expr ',' expr ',' expr ')'
        {
            exitLabel = new Label();
            leftLabel = new Label();
            rightLabel = new Label();
            topLabel = new Label();
            bottomLabel = new Label();
            successLabel = new Label();
                
            mv.visitLabel(rightLabel);        
            mv.visitVarInsn(Opcodes.ILOAD, 3);
            mv.visitInsn(Opcodes.I2D);
            mv.visitInsn(Opcodes.DCMPG);
            mv.visitJumpInsn(Opcodes.IFGE, bottomLabel);
            mv.visitInsn(Opcodes.POP2);
            mv.visitInsn(Opcodes.POP2);
            mv.visitInsn(Opcodes.POP2);
            mv.visitInsn(Opcodes.DCONST_0);
            mv.visitJumpInsn(Opcodes.GOTO, exitLabel);
            
            mv.visitLabel(bottomLabel);                    
            mv.visitVarInsn(Opcodes.ILOAD, 2);
            mv.visitInsn(Opcodes.I2D);
            mv.visitInsn(Opcodes.DCMPG);
            mv.visitJumpInsn(Opcodes.IFGE, leftLabel);
            mv.visitInsn(Opcodes.POP2);
            mv.visitInsn(Opcodes.POP2);
            mv.visitInsn(Opcodes.DCONST_0);
            mv.visitJumpInsn(Opcodes.GOTO, exitLabel);
            
            mv.visitLabel(leftLabel);  
            mv.visitVarInsn(Opcodes.ILOAD, 3);
            mv.visitInsn(Opcodes.I2D);
            mv.visitInsn(Opcodes.DCMPG);
            mv.visitJumpInsn(Opcodes.IFLT, topLabel);
            mv.visitInsn(Opcodes.POP2);
            mv.visitInsn(Opcodes.DCONST_0);
            mv.visitJumpInsn(Opcodes.GOTO, exitLabel);
                        
            mv.visitLabel(topLabel);  
            mv.visitVarInsn(Opcodes.ILOAD, 2);
            mv.visitInsn(Opcodes.I2D);
            mv.visitInsn(Opcodes.DCMPG);
            mv.visitJumpInsn(Opcodes.IFLT, successLabel);
            mv.visitInsn(Opcodes.DCONST_0);
            mv.visitJumpInsn(Opcodes.GOTO, exitLabel);           
            
            mv.visitLabel(successLabel); 
            mv.visitInsn(Opcodes.DCONST_1);
            mv.visitLabel(exitLabel);
        }
    |   PIXELINCIRCLE '(' expr ',' expr ',' expr ')'
        {
            successLabel = new Label();
            exitLabel = new Label();

            mv.visitInsn(Opcodes.DUP2);
            mv.visitInsn(Opcodes.DMUL);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, clz, "stack", "Ljava/util/List;");
            mv.visitInsn(Opcodes.DUP_X2);
            mv.visitInsn(Opcodes.POP);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z");
            mv.visitInsn(Opcodes.POP);
            
            mv.visitVarInsn(Opcodes.ILOAD, 3);
            mv.visitInsn(Opcodes.I2D);
            mv.visitInsn(Opcodes.DSUB);
            mv.visitInsn(Opcodes.DUP2);
            mv.visitInsn(Opcodes.DMUL);
            mv.visitInsn(Opcodes.DUP2_X2);
            mv.visitInsn(Opcodes.POP2);
            
            mv.visitVarInsn(Opcodes.ILOAD, 2);
            mv.visitInsn(Opcodes.I2D);
            mv.visitInsn(Opcodes.DSUB);
            mv.visitInsn(Opcodes.DUP2);
            mv.visitInsn(Opcodes.DMUL);
            mv.visitInsn(Opcodes.DADD);
            
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, clz, "stack", "Ljava/util/List;");
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "size", "()I");
            mv.visitInsn(Opcodes.ICONST_1);
            mv.visitInsn(Opcodes.ISUB);
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "remove", "(I)Ljava/lang/Object;");
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Double");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D");
            mv.visitInsn(Opcodes.DCMPG);
            mv.visitJumpInsn(Opcodes.IFLT, successLabel);
            mv.visitInsn(Opcodes.DCONST_0);
            mv.visitJumpInsn(Opcodes.GOTO, exitLabel);
            
            mv.visitLabel(successLabel);
            mv.visitInsn(Opcodes.DCONST_1);         
            mv.visitLabel(exitLabel);
        }
    |   PIXELONEDGE '(' expr ')'
        {
            exitLabel = new Label();
            leftLabel = new Label();
            rightLabel = new Label();
            topLabel = new Label();
            bottomLabel = new Label();
            failureLabel = new Label();
            
            mv.visitLabel(leftLabel);
            mv.visitInsn(Opcodes.DUP2);
            mv.visitVarInsn(Opcodes.ILOAD, 2);
            mv.visitInsn(Opcodes.I2D);
            mv.visitInsn(Opcodes.DCMPG);
            mv.visitJumpInsn(Opcodes.IFLE, rightLabel);
            mv.visitInsn(Opcodes.POP2);
            mv.visitInsn(Opcodes.DCONST_1);
            mv.visitJumpInsn(Opcodes.GOTO, exitLabel);
            
            mv.visitLabel(rightLabel);
            mv.visitInsn(Opcodes.DUP2);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, clz, "width", "I");
            mv.visitInsn(Opcodes.I2D);
            mv.visitInsn(Opcodes.DSUB);
            mv.visitInsn(Opcodes.DNEG);
            mv.visitVarInsn(Opcodes.ILOAD, 2);
            mv.visitInsn(Opcodes.I2D);
            mv.visitInsn(Opcodes.DCMPG);
            mv.visitJumpInsn(Opcodes.IFGE, topLabel);
            mv.visitInsn(Opcodes.POP2);
            mv.visitInsn(Opcodes.DCONST_1);
            mv.visitJumpInsn(Opcodes.GOTO, exitLabel);
            
            mv.visitLabel(topLabel);
            mv.visitInsn(Opcodes.DUP2);
            mv.visitVarInsn(Opcodes.ILOAD, 3);
            mv.visitInsn(Opcodes.I2D);
            mv.visitInsn(Opcodes.DCMPG);
            mv.visitJumpInsn(Opcodes.IFLE, bottomLabel);
            mv.visitInsn(Opcodes.POP2);
            mv.visitInsn(Opcodes.DCONST_1);
            mv.visitJumpInsn(Opcodes.GOTO, exitLabel);
              
            mv.visitLabel(bottomLabel);
            mv.visitInsn(Opcodes.DUP2);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, clz, "height", "I");
            mv.visitInsn(Opcodes.I2D);
            mv.visitInsn(Opcodes.DSUB);
            mv.visitInsn(Opcodes.DNEG);
            mv.visitVarInsn(Opcodes.ILOAD, 3);
            mv.visitInsn(Opcodes.I2D);
            mv.visitInsn(Opcodes.DCMPG);
            mv.visitJumpInsn(Opcodes.IFGE, failureLabel);
            mv.visitInsn(Opcodes.POP2);
            mv.visitInsn(Opcodes.DCONST_1);
            mv.visitJumpInsn(Opcodes.GOTO, exitLabel);
            
            mv.visitLabel(failureLabel);
            mv.visitInsn(Opcodes.POP2);
            mv.visitInsn(Opcodes.DCONST_0);  
                       
            mv.visitLabel(exitLabel);
        }
    |   PIXELAVERAGE '(' expr ',' expr ',' expr ',' expr ',' spec=(R|G|B|T|S) ')'
        {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, clz, "stack", "Ljava/util/List;");
            mv.visitInsn(Opcodes.DUP_X2);
            mv.visitInsn(Opcodes.POP);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z");
            mv.visitInsn(Opcodes.POP);
            
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, clz, "stack", "Ljava/util/List;");
            mv.visitInsn(Opcodes.DUP_X2);
            mv.visitInsn(Opcodes.POP);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z");
            mv.visitInsn(Opcodes.POP);
            
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, clz, "stack", "Ljava/util/List;");
            mv.visitInsn(Opcodes.DUP_X2);
            mv.visitInsn(Opcodes.POP);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z");
            mv.visitInsn(Opcodes.POP);
            
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, clz, "stack", "Ljava/util/List;");
            mv.visitInsn(Opcodes.DUP_X2);
            mv.visitInsn(Opcodes.POP);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z");
            mv.visitInsn(Opcodes.POP);
            
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, clz, "stack", "Ljava/util/List;");
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "size", "()I");
            mv.visitInsn(Opcodes.ICONST_1);
            mv.visitInsn(Opcodes.ISUB);
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "remove", "(I)Ljava/lang/Object;");
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Double");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D");
            
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, clz, "stack", "Ljava/util/List;");
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "size", "()I");
            mv.visitInsn(Opcodes.ICONST_1);
            mv.visitInsn(Opcodes.ISUB);
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "remove", "(I)Ljava/lang/Object;");
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Double");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D");
            
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, clz, "stack", "Ljava/util/List;");
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "size", "()I");
            mv.visitInsn(Opcodes.ICONST_1);
            mv.visitInsn(Opcodes.ISUB);
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "remove", "(I)Ljava/lang/Object;");
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Double");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D");
            
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, clz, "stack", "Ljava/util/List;");
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "size", "()I");
            mv.visitInsn(Opcodes.ICONST_1);
            mv.visitInsn(Opcodes.ISUB);
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "remove", "(I)Ljava/lang/Object;");
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Double");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D");
                                    
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "round", "(D)J");
            mv.visitInsn(Opcodes.L2I);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "abs", "(I)I");
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitInsn(Opcodes.ARRAYLENGTH);
            mv.visitInsn(Opcodes.IREM);
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitInsn(Opcodes.SWAP);
            mv.visitInsn(Opcodes.AALOAD);
            String s = $spec.text; 
            mv.visitLdcInsn(Character.valueOf(s.charAt(0))); 
            mv.visitInsn(Opcodes.SWAP);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, clz, "pixelAverage", "(DDDCLcom/mebigfatguy/pixelle/PixelleEval;)D");
        } ;


ABS             : 'ABS';
MAX             : 'MAX';
MIN             : 'MIN';
POW             : 'POW';
SQRT            : 'SQRT';
SIN             : 'SIN';
COS             : 'COS';
TAN             : 'TAN';
ASIN            : 'ASIN';
ACOS            : 'ACOS';
ATAN            : 'ATAN';
LOG             : 'LOG';    
EXP             : 'EXP';
E               : 'E';
PI              : 'PI';
RANDOM          : 'RANDOM';
PIXELINRECT     : 'PIXELINRECT';
PIXELINCIRCLE   : 'PIXELINCIRCLE';
PIXELONEDGE     : 'PIXELONEDGE';
PIXELAVERAGE    : 'PIXELAVERAGE';
X               : 'X';
Y               : 'Y';
WIDTH           : 'WIDTH';
HEIGHT          : 'HEIGHT';
P               : 'P';
R               : 'R';
G               : 'G';
B               : 'B';
T               : 'T';
S               : 'S';

	
NUMBER :   '0'..'9'+ ( '.' ('0'..'9'+))?;

WS :       (' '|'\t'|'\f'|'\n'|'\r')+{ skip(); };  



