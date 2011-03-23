Java Text Extractor API
=======================

Web API for Java based text extractors. Implemented using Play framework.

Author
======

Tomaž Kovačič <tomaz.kovacic@gmail.com>

Extractors supported
====================

- `Boilerpipe <http://code.google.com/p/boilerpipe/>`_
- `Goose <https://github.com/jiminoc/goose/>`_ *(on TODO list)*

API Documentation
=================


Boilerpipe API
--------------

method: ``POST``

endpoint: ``http://yourdomain//boilerpipe/extract/``

params:
- ``extractorType`` : ``(article|default)``
- ``rawHtml`` : html content

JSON response format: 

::

	{	
		"result": RESULT_TEXT
		"status": (OK|ERROR)
		"errorMsg": ERROR_MESSAGE (optional)
	}	


Dependencies
============

- `Play <http://www.playframework.org/>`_ framework v1.1.1.

Licence
=======

- Everything that's not in the ``/lib/`` directory is licenced under GPLv3
- Jar packages in the ``/lib/`` directory are all licenced under Apache Licence 2.0:
    + `Boilerpipe <http://code.google.com/p/boilerpipe/>`_
    + `NekoHTML <http://nekohtml.sourceforge.net/>`_
    + `Xerces <http://xerces.apache.org/>`_


Copyright (C) Tomaž Kovačič

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
