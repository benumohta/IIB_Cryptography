package com.ibm.broker.crytpo;

import org.eclipse.swt.widgets.Composite;

import com.ibm.broker.config.appdev.Node;
import com.ibm.broker.config.appdev.NodeProperty;
import com.ibm.etools.mft.api.AbstractPropertyEditor;
import com.ibm.etools.mft.api.IRuntimePropertyCompiler;
import com.ibm.etools.mft.ibmnodes.compilers.RuntimePropertyCompilerException;

public class EnumeratedPropertyController extends AbstractPropertyEditor
		implements IRuntimePropertyCompiler {

	@Override
	public void createControls(Composite parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String isValid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCurrentValue(Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String compile(Object arg0) throws RuntimePropertyCompilerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRequiredConfigurable(Node arg0, NodeProperty arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWhiteSpacePreserved() {
		// TODO Auto-generated method stub
		return false;
	}

}
