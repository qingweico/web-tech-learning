import React, {useState, useRef} from "react";
import {Calendar} from 'primereact/calendar';
import {Divider} from 'primereact/divider';
import {Accordion, AccordionTab} from 'primereact/accordion';
import { SplitButton } from 'primereact/splitbutton';
import { Toast } from 'primereact/toast';
export default function PrimeReactApp() {
    const [date, setDate] = useState(null);
    const toast = useRef(null);
    const items = [
        {
            label: 'Update',
            icon: 'pi pi-refresh',
            command: () => {
                toast.current.show({ severity: 'success', summary: 'Updated', detail: 'Data Updated' });
            }
        },
        {
            label: 'Delete',
            icon: 'pi pi-times',
            command: () => {
                toast.current.show({ severity: 'warn', summary: 'Delete', detail: 'Data Deleted' });
            }
        },
        {
            label: 'React Website',
            icon: 'pi pi-external-link',
            command: () => {
                window.location.href = 'https://reactjs.org/';
            }
        },
        {
            label: 'Upload',
            icon: 'pi pi-upload',
            command: () => {
                toast.current.show({ severity: 'success', summary: 'Upload', detail: 'Upload Success' });
            }
        }
    ];

    const save = () => {
        toast.current.show({ severity: 'success', summary: 'Success', detail: 'Data Saved' });
    };
    return (<div className="card flex justify-content-center">
        <Calendar value={date} onChange={(e) => setDate(e.value)} showButtonBar showIcon/>
        <Divider/>
        <div className="card">
            <Accordion activeIndex={2}>
                <AccordionTab header="第一部分">
                    <p className="m-0">
                        第一部分
                    </p>
                </AccordionTab>
                <AccordionTab header="第二部分">
                    <p className="m-0">
                        第二部分
                    </p>
                </AccordionTab>
                <AccordionTab header="第三部分">
                    <p className="m-0">
                        第三部分
                    </p>
                </AccordionTab>
            </Accordion>
        </div>
        <Divider/>
        <div className="card flex justify-content-center">
            <Toast ref={toast}></Toast>
            <SplitButton label="Save" icon="pi pi-plus" onClick={save} model={items}/>
        </div>
    </div>)
}
