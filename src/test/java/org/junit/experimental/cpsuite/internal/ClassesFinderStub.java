package org.junit.experimental.cpsuite.internal;

import static java.util.Collections.emptyList;

import java.util.List;

public final class ClassesFinderStub extends ClassesFinder {

	public ClassesFinderStub() {
		super(null, null);
	}

	private boolean fCalled;

	private List<Class<?>> fResult= emptyList();

	@Override
	public List<Class<?>> find() {
		fCalled= true;
		return fResult;
	}

	public boolean wasCalled() {
		return fCalled;
	}

	public void setResult(List<Class<?>> result) {
		fResult= result;
	}
}